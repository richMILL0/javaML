import java.nio.charset.StandardCharsets;
import java.util.*;

record Player(String name, Integer rank) {}
record Match(Player firstPlayer, Player secondPlayer) {}
record Result(String predict, String real) {
    @Override 
    public String toString() {
        return predict() + " | " + real();
    }
}

public static void main() throws IOException {
    Path path = Paths.get("matches_after_proc.csv");
    List<Match> matches = new ArrayList<>();
    List<String> reals = new ArrayList<>();

    try(Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
        stream
        .filter(str -> !str.startsWith("match_id"))
        .forEach(str -> {
            String[] parameters = str.split(",");
            for (String v : parameters) {
                if(v == null || v.length() == 0) {
                    return;
                }
            }
            matches.add(new Match(new Player(parameters[3], Integer.parseInt(parameters[5])), new Player(parameters[4], Integer.parseInt(parameters[6]))));

            String real = Integer.parseInt(parameters[7]) > Integer.parseInt(parameters[8]) ? parameters[3] : parameters[4];
            reals.add(real);
        });
    }

    List<String> result = new ArrayList<>();
    for (Match match : matches) {
        result.add(WinPredictor.predict(match.firstPlayer(), match.secondPlayer()));
    }

    Double counter = 0.0;
    for (int i = 0 ; i < result.size() ; ++i) {
        if (result.get(i).equals(reals.get(i))) {
            counter++;
        }

        System.out.println("Предикт=" + result.get(i) + " | Результат=" + reals.get(i));
    }

    System.out.println(counter / result.size());
}

class WinPredictor {
    public static String predict(
        Player firstPlayer, 
        Player secondPlayer
    ) {
        if (firstPlayer.name() == null || secondPlayer.name() == null) {
            return "UNCORRECT DATA";
        }

        if (firstPlayer.rank() == null || secondPlayer.rank() == null) {
            return "SKIP";
        }

        if (firstPlayer.name().equals(secondPlayer.name())) {
            return "UNCORRECT DATA";
        }

        if (firstPlayer.rank().equals(secondPlayer.rank())) {
            return "SKIP";
        }

        if (firstPlayer.rank() > secondPlayer.rank()) {
            return secondPlayer.name();
        } else {
            return firstPlayer.name();
        }
    }
}
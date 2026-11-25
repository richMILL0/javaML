import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.IOException;

record Player(String name, Integer rank) {}
record Match(Player firstPlayer, Player secondPlayer) {}
record Result(String predict, String real) {
    @Override 
    public String toString() {
        return predict() + " | " + real();
    }
}

class Terminal {
    public static void main() throws IOException {
        Path path = Paths.get("atp_matches_2022_2023_drop_dup.csv");
        Pattern reg = Pattern.compile("[A-Za-z]");
        
        int[] counts = new int[2];
        try(Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream
            .filter(str -> !str.startsWith("match_id"))
            .forEach(str -> {
                String[] parameters = str.split(",");
    
                if (parameters.length < 5) {
                    return;
                }
    
                for (String v : parameters) {
                    if(v == null || v.length() == 0) {
                        return;
                    }
                }
    
                if (reg.matcher(parameters[2]).find() || 
                    Double.parseDouble(parameters[3]) <= 0.0 || 
                    Objects.equals(Double.parseDouble(parameters[3]), Double.parseDouble(parameters[4]))
                ) {
                    return;
                }
    
                int winner_rank = (int)Double.parseDouble(parameters[3]);
                int loser_rank = (int)Double.parseDouble(parameters[4]);
    
                // matches.add(new Match(new Player(parameters[3], Integer.parseInt(parameters[5])), new Player(parameters[4], Integer.parseInt(parameters[6]))));
                int gap = Math.abs(winner_rank - loser_rank);
                if (gap < 16) {
                    counts[0]++;
                } else {
                    counts[1]++;
                }
            });
        }
    
        System.out.println("closest=" + counts[0] + " farthest=" + counts[1]);
        // List<String> result = new ArrayList<>();
        // for (Match match : matches) {
        //     result.add(WinPredictor.predict(match.firstPlayer(), match.secondPlayer()));
        // }
    }
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
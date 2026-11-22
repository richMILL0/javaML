import java.util.*;

record Player(String name, Integer rank) {}
record Match(Player first, Player second) {}
record Result(String predict, String real) {
    @Override 
    public String toString() {
        return predict() + " | " + real();
    }
}

public static void main() {
    Map<Match, Result> statistic = new LinkedHashMap<>();     // match | result

    statistic.put(new Match(new Player("Orlov", 18), new Player("Costa", 42)), null);
    statistic.put(new Match(new Player("Vega", 55), new Player("Kim", 24)), null);
    statistic.put(new Match(new Player("Kim", 24), new Player("Orlov", 18)), null);
    statistic.put(new Match(new Player("Costa", 42), new Player("Vega", 55)), null);
    statistic.put(new Match(new Player("Vega", null), new Player("Orlov", 18)), null);
    statistic.put(new Match(new Player("Costa", 42), new Player("Kim", 24)), null);
    statistic.put(new Match(new Player("Orlov", 18), new Player("Vega", 55)), null);
    statistic.put(new Match(new Player("Kim", 24), new Player("Costa", 42)), null);

    List<String> reals = List.of(
        "Orlov",
        "Vega",
        "Orlov",
        "Vega",
        "Orlov",
        "Kim",
        "Vega",
        "Kim"
    );

    var it1 = statistic.entrySet().iterator();
    var it2 = reals.iterator();
    while (it1.hasNext()) {
        Map.Entry<Match, Result> entry = it1.next();
        String real = it2.next();
        Result result = new Result(WinPredictor.predict(entry.getKey().first(), entry.getKey().second()), real);
        entry.setValue(result);
    }

    statistic.entrySet().forEach(entry -> {
        System.out.println(entry.getValue());
    });
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
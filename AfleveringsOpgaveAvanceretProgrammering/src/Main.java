public class Main {
    public static void main(String[] args) {
        Dictionary<String, Integer> d = new DictionaryOpenAddressing<>();

        d.put("æble", 1);
        d.put("banan", 2);
        d.put("pære", 3);

        System.out.println(d.get("banan"));   // 2
        d.remove("banan");
        System.out.println(d.get("banan"));   // null
        System.out.println(d.size());         // 2
        System.out.println(d);                // {æble=1, pære=3}
    }
}
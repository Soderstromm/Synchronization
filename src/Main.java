import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        int length = 100;
        int threadsCount = 1000;
        String letters = "RLRFR";

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute(letters, length);
                int countR = countSymbols(route, 'R');

                synchronized (sizeToFreq) {
                    sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Поиск часто встречающегося количества букв R
        int mostFreqCount = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        int mostFrequentFreq = sizeToFreq.get(mostFreqCount);

        System.out.println("Самое частое количество повторений " + mostFreqCount + " (встретилось " + mostFrequentFreq + " раз)");
        System.out.println("Другие размеры:");

        sizeToFreq.entrySet().stream()
                .filter(entry -> entry.getKey() != mostFreqCount)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)"));

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    // Подсчет количества символов в строке
    public static int countSymbols(String str, char target) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }
}
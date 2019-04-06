package io.xylite.log_error_ratio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please include a logfile");
            System.exit(1);
        }
        long start = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {

            Map<String, Double> hosts = reader.lines().parallel()
                    .map(s -> s.split(" \\| "))
                    .filter(a -> a.length > 4)
                    .collect(
                            Collectors.groupingBy(
                                    (String[] a) -> a[2],
                                    Collectors.mapping(
                                            (String[] a) -> a[4],
                                            Collectors.averagingDouble(status -> status.startsWith("5") ? 1 : 0)
                                    )
                            )
                    );

            hosts.entrySet().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}

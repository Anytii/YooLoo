package strategies;

import common.YoolooKarte;
import logging.Logging;
import server.YoolooClientHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Strategies {
	
	private static final transient Logging LOGGER = new logging.Logging(YoolooClientHandler.class.getName());

    public static YoolooKarte[] genStratAI(YoolooKarte[] oldSort, String name) {
        if (!Files.notExists(Path.of(name + ".txt"))) {
            ArrayList<String> importedDataLastRound = new ArrayList<>();
            YoolooKarte[] newSort = new YoolooKarte[oldSort.length];
            Map<String, Integer> fileMap = new HashMap<>();

            try {
                for (String convertString : Files.readAllLines(Path.of(name + ".txt"))) {
                    String[] line = convertString.split("::");
                    fileMap.put(line[0], Integer.parseInt(line[1]));
                    newSort[Integer.parseInt(line[0])] = new YoolooKarte(oldSort[Integer.parseInt(line[0])].getFarbe(), Integer.parseInt(line[1]));
                }
                for (int i = 0; i < newSort.length; i++) {
                    if (!fileMap.containsKey(i)) {
                        int j = 0;
                        while (j< newSort.length-1 && newSort[j] != null) {
                            j++;
                        }
                        newSort[j] = new YoolooKarte(oldSort[0].getFarbe(), i);
                    }
                }
                PrintWriter writer = new PrintWriter(new File(name+".txt"));
                writer.print("");
                writer.close();

            } catch (IOException e) {
                LOGGER.log(e.getStackTrace().toString());
                genStratRandom(oldSort, name);
            }
           // System.out.println(newSort);
            LOGGER.log(newSort.toString());
            return newSort;
        } else {
            return genStratRandom(oldSort, name);
        }
    }

    public static YoolooKarte[] genStratRandom(YoolooKarte[] oldSort, String name) {
        YoolooKarte[] neueSortierung = new YoolooKarte[oldSort.length];
        for (int i = 0; i < neueSortierung.length; i++) {
            int neuerIndex = (int) (Math.random() * neueSortierung.length);
            while (neueSortierung[neuerIndex] != null) {
                neuerIndex = (int) (Math.random() * neueSortierung.length);
            }
            neueSortierung[neuerIndex] = oldSort[i];
            // System.out.println(i+ ". neuerIndex: "+neuerIndex);
        }
        return neueSortierung;
    }
}

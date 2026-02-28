package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

record TraducaoObj(
        String original,
        int original_length,
        String traducao
        ){}

public class Main {
    static ObjectMapper om;
    static Path filePath;

    static void main() throws IOException {
        om = new ObjectMapper();
        filePath = Path.of("translations.json");

        var loadedTraducoesFromFile = new ArrayList<>(loadTraducoesFromFile());

        var input = new Scanner(System.in);

        IO.println("Digite \"sair\" para sair");

        while (true) {
            IO.println("Informe o texto original");
            var originalText = input.nextLine();

            if (originalText.equalsIgnoreCase("sair")) break;

            IO.println("Informe o texto traduzido");
            StringBuilder traduzidoText = new StringBuilder(input.nextLine());

            int tam1 = originalText.length();
            int tam2 = traduzidoText.length();

            IO.println("texto original: " + originalText + " ;tam: " + tam1);
            IO.println("texto traduzido: " + traduzidoText + " ;tam: " + tam2);

            //normalizacoes
            adicionaUnderlineSeNecessario(tam2, tam1, traduzidoText);
            removeCaracteresAMais(tam2, tam1, traduzidoText);

            String finalText = getFinalText(traduzidoText);

            IO.println("Texto final: ");
            IO.println(finalText);
            IO.println("--------------------------");

            loadedTraducoesFromFile.add(new TraducaoObj(originalText, tam1, finalText));
            saveTraducoesInFile(loadedTraducoesFromFile);
        }

        input.close();

        //fileStream.close();
    }

    private static String getFinalText(StringBuilder traduzidoText) {
        String finalText = traduzidoText.toString();
        finalText = finalText.replaceAll("_", " ");
        return finalText;
    }

    private static void removeCaracteresAMais(int tam2, int tam1, StringBuilder traduzidoText) {
        if (tam2 > tam1) {
            int dif = tam2 - tam1;
            IO.println(String.format("Texto traduzido pode perder: %d espaços", dif));

            for (int i = 0; i < dif; i++) {
                int index = traduzidoText.indexOf("_");
                traduzidoText.deleteCharAt(index);
            }

            IO.println("texto traduzido(FIX): " + traduzidoText + " ;novo tam: " + traduzidoText.length());
        }
    }

    private static void adicionaUnderlineSeNecessario(int tam2, int tam1, StringBuilder traduzidoText) {
        if (tam2 < tam1) {
            int dif = tam1 - tam2;
            IO.println(String.format("Texto traduzido pode receber: %d espaços", dif));

            for (int i = 0; i < dif; i++) {
                traduzidoText.append("_");
            }
        }
    }

    static List<TraducaoObj> loadTraducoesFromFile() throws IOException {
        return om.readValue(
                filePath.toFile(),
                new TypeReference<List<TraducaoObj>>() {
                }
        );
    }

    static void saveTraducoesInFile(List<TraducaoObj> traducoesList) throws IOException {
        om.writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), traducoesList);
        IO.println("Traducao foi salva!");
    }
}

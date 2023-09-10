import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String inputFile = "input/Data Alert.txt";
        String institutionParameter = "MDR";
        StringBuilder outputMessage = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            outputMessage.append("Selamat Siang rekan Bank ").append(institutionParameter).append(",\n\n");
            outputMessage.append("Mohon bantuannya untuk Sign on pada envi berikut :\n\n");

            String line;
            boolean isDataEmpty = true;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length >= 5) {
                    String institution = fields[0];
                    String env = fields[1];
                    int port = Integer.parseInt(fields[2]);
                    String status = fields[4];

                    if ("Offline".equalsIgnoreCase(status) && institutionParameter.equalsIgnoreCase(institution)) {
                        outputMessage.append(generateOutputMessage(env, port, status));
                        isDataEmpty = false;
                    }
                }
            }

            outputMessage.append("\nTerima kasih");
            if (!isDataEmpty)
                sendOutputMessage(outputMessage.toString());
            else
                System.out.println("Tidak ada data untuk rekan Bank "+institutionParameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateOutputMessage(String env, int port, String status) {
        return "- Envi " + env + " Port " + port + " terpantau " + status + "\n";
    }

    private static void sendOutputMessage(String message) {
        System.out.println(message);
    }
}

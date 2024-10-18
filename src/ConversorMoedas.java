

import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class ConversorMoedas {


    // Substitua "SUA_API_KEY_AQUI" pela chave obtida no site ExchangeRate-API
    private static final String API_KEY = "a87c46bf83-d43690fcc2-slhlv1i";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Conversor de Moedas!");
        System.out.println("Escolha a conversão desejada:");
        System.out.println("1 - USD para BRL");
        System.out.println("2 - EUR para BRL");
        System.out.println("3 - BRL para USD");
        System.out.println("4 - BRL para EUR");
        System.out.println("5 - USD para EUR");
        System.out.println("6 - EUR para USD");

        int escolha = scanner.nextInt();
        scanner.nextLine();  // Consumir a quebra de linha

        String fromCurrency = "";
        String toCurrency = "";

        switch (escolha) {
            case 1 -> { fromCurrency = "USD"; toCurrency = "BRL"; }
            case 2 -> { fromCurrency = "EUR"; toCurrency = "BRL"; }
            case 3 -> { fromCurrency = "BRL"; toCurrency = "USD"; }
            case 4 -> { fromCurrency = "BRL"; toCurrency = "EUR"; }
            case 5 -> { fromCurrency = "USD"; toCurrency = "EUR"; }
            case 6 -> { fromCurrency = "EUR"; toCurrency = "USD"; }
            default -> {
                System.out.println("Escolha inválida. Encerrando o programa.");
                return;
            }
        }

        System.out.print("Digite o valor a ser convertido: ");
        double valor = scanner.nextDouble();

        double taxa = obterTaxaDeCambio(fromCurrency, toCurrency);
        if (taxa != -1) {
            double valorConvertido = valor * taxa;
            System.out.printf("Valor convertido: %.2f %s\n", valorConvertido, toCurrency);
        } else {
            System.out.println("Não foi possível obter a taxa de câmbio.");
        }
    }

    // Método para obter a taxa de câmbio da API
    private static double obterTaxaDeCambio(String from, String to) {
        try {
            URL url = new URL(BASE_URL + from);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Erro ao conectar à API.");
                return -1;
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject json = new JSONObject(inline.toString());
            return json.getJSONObject("conversion_rates").getDouble(to);

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return -1;
        }
    }


}

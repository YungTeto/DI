package gelato.Mails;

import gelato.Kunden.Kunde;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public interface IMail {
    void sendMail(Kunde kunde, ProcessBuilder processBuilder);

    Mail createMail(String name, LocalDateTime localDateTime, DayOfWeek dayOfWeek, boolean istHeiss, boolean istTrocken, boolean istKalt);

    Mail pannaCotta(String name);

    Mail tiramisu(String name);

    Mail cassata(String name);

    Mail zuppaInglese(String name);

    Mail waffelnUndKaffee(String name);

    Mail gelato(String name);

    Mail granita(String name);

    void updateLastMailDate(Kunde kunde);

    void sendMarketingMail(Kunde kunde);
}

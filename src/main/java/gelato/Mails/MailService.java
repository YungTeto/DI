package gelato.Mails;

import gelato.Kunden.Kunde;
import gelato.Wetter.Wetter;
import gelato.Wetter.WetterService;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.time.DayOfWeek.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MailService implements IMail {

    private final String covid = "\n\nPS Wir haben natürlich alles für die Hygiene getan. Unsere Tische haben einen Mindestabstand von 2.5m, wir haben Scheiben zwischen den Tischen und alle unsere Bedienungen tragen Masken.";

    private WetterService wetterservice;

    private Wetter wetter;

    private final Connection connection;



    public MailService(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void sendMail(Kunde kunde, ProcessBuilder processBuilder) {

        //todo unsicher
        Mail mail = createMail(kunde.getName(), LocalDateTime.now(), LocalDateTime.now().getDayOfWeek(), wetter.istHeiss(), wetter.istTrocken(), wetter.istKalt());
        Process process = null;
        try {
            process = processBuilder
                    .command("java", "-jar", Paths.get("lib", "ext", "mail.jar").toString(), kunde.getEmail(),
                            mail.getSubject(),
                            mail.getBody())
                    .redirectOutput(INHERIT)
                    .redirectError(INHERIT)
                    .start();
            process.waitFor(5, SECONDS);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mail createMail(String name, LocalDateTime localDateTime, DayOfWeek dayOfWeek, boolean istHeiss, boolean istTrocken, boolean istKalt) {
        Wetter wetter  = wetterservice.wetterDaten(new ProcessBuilder());

        //fertig
        LocalDateTime now = localDateTime;
        //todo DI unsicher
        DayOfWeek tag = dayOfWeek;

        boolean frueh = now.getHour() > 6 && now.getHour() < 10;

        // Wetter Logic
        if (istHeiss && istTrocken) {
            if (frueh) {
                return granita(name);
            }
            return gelato(name);
        }
        else if (istKalt) {
            return waffelnUndKaffee(name);
        }
        else {
            if (tag.equals(SATURDAY) || tag.equals(SUNDAY)) {
                return zuppaInglese(name);
            }
            if (tag.equals(MONDAY) || tag.equals(WEDNESDAY)) {
                return cassata(name);
            }
            if (tag.equals(TUESDAY) || tag.equals(THURSDAY)) {
                return tiramisu(name);
            }
            if (tag.equals(FRIDAY)) {
                return pannaCotta(name);
            }
        }
        return new Mail("", "");
    }

    public Mail pannaCotta(String name) {
        String subject = "Es gibt hausgemachte Panna Cotta";
        String body = String.format("Hallo %s,%n%nendlich ist Freitag! " +
                "Und zur Feier des Tages haben wir unsere hausgemachte Panna Cotta im Angebot.%n\n" +
                "Du hast die freie Wahl, ob du die original Panna Cotta mit herrlich frischem Kompptt " +
                "oder Caramellsoße haben willst.%n%nDas Angebot gilt nur solange unser Vorrat reicht.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail tiramisu(String name) {
        String subject = "Magst du ein leckeres Tiramisu?";
        String body = String.format("Hallo %s,%n%n" +
                "kann es etwas besseres geben, als ein Tiramisu nach Originalrezept mit frischer Mascarpone Creme? Wir haben heute " +
                "den Klassiker aus Italien im Angebot. Eine herrlich süße Schleckerei, die wir gerne mit einer Tasse Cappucino servieren." +
                "%n%nDas Angebot gilt nur solange unser Vorrat reicht.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail cassata(String name) {
        String subject = "Die Bombe aus Italien: Cassata";
        String body = String.format("Hallo %s,%n%n" +
                "unser hausgemachtes Cassata Eis ist der original Cassata alla siciliana nachempfunden. Sie besteht aus  Himbeer-, Vanille- und Schokoladeneis sowie kandierten Früchten%n%n" +
                "%n%nDas Angebot gilt nur solange unser Vorrat reicht.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail zuppaInglese(String name) {
        String subject = "Tante Irma hat Zuppa Inglese gemacht";
        String body = String.format("Hallo %s,%n%n" +
                "dieses Wochenende hat Tante Irma ihre berühmte Zuppa Inglese gemacht. Lass dir die Spezialität aus Italiens Norden nicht entgehen.%n%n" +
                "%n%nDas Angebot gilt nur solange unser Vorrat reicht.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail waffelnUndKaffee(String name) {
        String subject = "Mistwetter? Egal!";
        String body = String.format("Hallo %s,%n%n" +
                "lass dich vom Regen und der Kälte nicht runterziehen. Wir haben genau das Richtige! Leckere hausgemachte Waffeln und einen großen Pott Kaffee.%n%n" +
                "%n%nKomm einfach vorbei und nimm eine Auszeit.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail gelato(String name) {
        String subject = "Warm! Warm! Warm!";
        String body = String.format("Hallo %s,%n%n" +
                "ist dir auch so heiß? Wir haben genau das Richtige! Leckeres hausgemachtes Eis in Meisterqualität.%n%n" +
                "%n%nKomm einfach vorbei und kühl dich bei einem original italienischem Gelato ab.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }

    public Mail granita(String name) {
        String subject = "Frühstücken wie in Sizilien?";
        String body = String.format("Hallo %s,%n%n" +
                "Wenn es in Sizilien warm ist, dann frühstücken wir ein Granita al limone. Ein fruchtiges Zitronensorbet und dazu gibt es ein Brioche und Espresso.%n%n" +
                "%n%nKomm vorbei und starte den Tag wie im Urlaub.%n%n" +
                "Wir freuen uns auf Dich!%nDein Team von Gelateria Giacomo%n", name);
        body += covid;
        return new Mail(subject, body);
    }


    @Override
    public void updateLastMailDate(Kunde kunde) {
        try {
            PreparedStatement stmt =
                    connection.prepareStatement("UPDATE kunden SET lastmail = CURRENT_DATE WHERE id = ?");
            stmt.setLong(1, kunde.getId());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void sendMarketingMail(Kunde kunde) {
        updateLastMailDate(kunde);
        sendMail(kunde, new ProcessBuilder());
    }
}

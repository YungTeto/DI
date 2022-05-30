package gelato.Kunden;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KundenService implements IKunde {

    private final Connection connection;

    public KundenService(Connection connection) {
        this.connection = connection;
    }

    public List<Kunde> getKunden() {
        ArrayList<Kunde> kunden = new ArrayList<>();
        String sql = "SELECT * FROM kunden";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("mail");
                Date date = resultSet.getDate("lastmail");
                Kunde kunde = new Kunde(id, name, email, date.toLocalDate());
                kunden.add(kunde);
            }
        } catch (SQLException throwables) {
            System.err.println("Fehler beim Laden der Kundendaten");
            System.exit(-1);
        }
        return kunden;
    }
}

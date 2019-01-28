package de.maindefense;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import me.legrange.haveibeenpwned.Breach;
import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;

public class Start {

    public static void main(String[] args)  {

        Start s = new Start();
        List<String> accounts;
        try {
            accounts = s.getAccounts();
            final HaveIBeenPwndApi hibp = new HaveIBeenPwndApi("de.maindefense.hibp-breach-checker");

            CSVFormat format = CSVFormat.EXCEL;
            CSVPrinter printer = new CSVPrinter(Files.newBufferedWriter(Paths.get("results.csv")), format);
            accounts.forEach(a -> {
                try {
                    List<Breach> breaches = hibp.getAllBreachesForAccount(a);
                    breaches.forEach(b -> {
                        try {
                            printer.printRecord(a, b.getBreachDate(), b.getAddedDate(), b.getName(), b.getTitle(), b.getDomain(),
                                    b.getDescription());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (HaveIBeenPwndException e) {
                    e.printStackTrace();
                }

            });
            printer.flush();
            System.out.println("done");
            System.exit(0);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public List<String> getAccounts() throws IOException {
        return Files.readAllLines(Paths.get("accounts.txt"));
    }

}

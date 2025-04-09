import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// F-> COMENTO SOBRE EL MATEIX CODI 

public class LecturaFitxerRegio {
    private Map<String, Regio> regions; // F-> NO VEIG MOLT BÉ QUE AQUESTA CLASSE USI LA CLASSE REGIÓ 

    public LecturaFitxerRegio() {
        this.regions = new HashMap<>();
    }

    public void llegirArxiuRegio(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        boolean enSeccioLimits = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            // F-> NO ÉS MOLT "NET" UN CODI AMB TANTS CONTINUE. MILLOR Q BUSQUIS UNA ALTERNATIVA (I ÉS FÀCIL :) )
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            if (line.equals("*")) {
                //enSeccioLimits = false;
                continue;
            }
            
            if (line.equals("limits_i_mobilitat")) {
                enSeccioLimits = true;
                continue;
            }

            if (!enSeccioLimits) {
                processarDadesRegio(line, br);
            } else {
                processarLimitsMobilitat(line, br);
            }
        }
        br.close();
    }

    private void processarDadesRegio(String line, BufferedReader br) throws IOException {

        if (!line.startsWith("nom ")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava 'nom'.");
        }
        String nom = line.substring(4).trim().split(" ")[0]; // F-> NO ES POT FER MÉS SIMPLE?
        
        line = br.readLine().trim();
        if (!line.startsWith("habitants ")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava 'habitants'.");
        }
        int habitants = Integer.parseInt(line.substring(10).trim().split(" ")[0]); //F-> NO ES POT FER MÉS SIMPLE?
        
        line = br.readLine().trim();
        if (!line.startsWith("mob_interna ")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava 'mob_interna'.");
        }
        double mobInterna = Double.parseDouble(line.substring(11).trim().split(" ")[0]); //F-> NO ES POT FER MÉS SIMPLE?
        
        regions.put(nom, new Regio(nom, habitants, mobInterna)); 
    }

    private void procesarLimitsMobilitat(String line, BufferedReader br) throws IOException {
        String regioOrigen = line.trim();
        
        if (!regions.containsKey(regioOrigen)) {
            throw new IOException("Regió d'origen no trobada: " + regioOrigen);
        }
        
        Regio regio = regions.get(regioOrigen);
        
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (linea.equals("*")) {
                break;
            }
            
            String[] parts = line.split(" ");
            if (parts.length != 2) {
                throw new IOException("Format incorrecte en linea de secció de limits: " + linea);
            }
            
            String regioDesti = parts[0];
            double taxaMobilitat = Double.parseDouble(parts[1]);
            
            if (!regions.containsKey(regioDesti)) {
                throw new IOException("Regió destí no trobada: " + regioDesti);
            }
            
            regio.agregarConexio(regioDesti, taxaMobilitat);//Metode necessari en classe regio
        }
    }

    public Map<String, Regio> getRegions() {
        return regions;
    }

}

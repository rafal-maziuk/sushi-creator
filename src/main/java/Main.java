import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum ESkladnik {
    Avocado, ButterFish, Caviar, Kanpyo, Kappa, Nori, Oshinko, Rice, Salmon, Shrimp, Taco, Tuna;
}

enum ESushi {

    KappaMaki("Kappa Maki", ESkladnik.Kappa, ESkladnik.Rice, ESkladnik.Nori),
    SalmonMaki("Salmon Maki", ESkladnik.Salmon, ESkladnik.Rice, ESkladnik.Nori),
    OshinkoMaki("Oshinko Maki", ESkladnik.Oshinko, ESkladnik.Rice, ESkladnik.Nori),
    KanpyoMaki("Kanpyo Maki", ESkladnik.Kanpyo, ESkladnik.Rice, ESkladnik.Nori),
    IbodaiNigiri("Ibodai Nigiri", ESkladnik.ButterFish, ESkladnik.Rice),
    GunkanCaviar("Gunkan Caviar", ESkladnik.Caviar, ESkladnik.Rice, ESkladnik.Nori),
    PhiladelphiaSalmonMaki("Philadelphia Salmon Maki", ESkladnik.Salmon, ESkladnik.Avocado,
            ESkladnik.Rice, ESkladnik.Nori),
    VegeFutomaki("Vege Futomaki", ESkladnik.Kappa, ESkladnik.Rice, ESkladnik.Avocado,
            ESkladnik.Oshinko, ESkladnik.Nori, ESkladnik.Kanpyo);

    private String nazwa;
    private List<ESkladnik> listaSkladnikow;

    private ESushi(String nazwa, ESkladnik ... skladniki) {
        this.nazwa = nazwa;
        this.listaSkladnikow = Arrays.asList(skladniki);
    }
    public String getNazwa(){
        return nazwa;
    }
    public List<ESkladnik> getListaSkladnikow(){
        return listaSkladnikow;
    }
}

public class Main extends JPanel{

    private int punkty = 0;
    private int zycia = 5;

    private List<ESkladnik> listaSkladnikow;
    private JLabel tekst;
    private ESushi wylosowana;

    public Main() {

        try {
            this.listaSkladnikow = new ArrayList<ESkladnik>();

            setBackground(Color.white);
            setLayout(null);
            stworzPanelKlawiszy();
            stworzPanelTekstu();
            stworzKlawiszCheck();
            stworzKlawiszX();
        } catch (Exception e) {}
    }

    private void stworzKlawiszX() throws IOException {
        JLabel klawisz = new JLabel();
        klawisz.setIcon(new ImageIcon(ImageIO.read(new File("x.png"))));
        klawisz.setBounds(580,100,100,50);

        klawisz.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                listaSkladnikow.clear();
                repaint();
            }
        });
        add(klawisz);
    }

    private void stworzKlawiszCheck() throws IOException {
        JLabel klawisz = new JLabel();
        klawisz.setIcon(new ImageIcon(ImageIO.read(new File("check.png"))));
        klawisz.setBounds(530,50,200,50);

        klawisz.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Collections.sort(listaSkladnikow);
                Collections.sort(wylosowana.getListaSkladnikow());

                if(listaSkladnikow.equals(wylosowana.getListaSkladnikow())){
                    punkty++;
                    listaSkladnikow.clear();
                    losuj();
                } else {
                    zycia--;
                    listaSkladnikow.clear();
                }
                repaint();
                czyKoniecGry();
            }
            private void czyKoniecGry(){
                if(zycia < 1) {
                    JOptionPane.showMessageDialog(null,"Twoj wynik: " + punkty);
                    zycia = 5;
                    losuj();
                    punkty = 0;
                    repaint();
                }
            }
        });
        add(klawisz);
    }

    private void stworzPanelTekstu(){
        tekst = new JLabel();
        tekst.setBounds(100, 35, 400, 80);
        Font font = new Font("Aerial", Font.BOLD, 25);

        losuj();

        tekst.setFont(font);
        add(tekst);
    }

    private void losuj(){
        int index = (int)(Math.random() * ESushi.values().length);
        wylosowana = ESushi.values()[index];
        tekst.setText(wylosowana.getNazwa());
    }

    private void stworzPanelKlawiszy() throws IOException {
        JPanel panelKlawiszy = new JPanel();
        panelKlawiszy.setLayout(new GridLayout(3,4));

        File[] grafika = new File("Skladniki/").listFiles();

        for (File f : grafika) {
            JLabel klawisz = new JLabel();

            klawisz.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    listaSkladnikow.add(ESkladnik.valueOf(f.getName().substring(0, f.getName().length() - 4)));
                    repaint();
                }
            });
            klawisz.setIcon(new ImageIcon(ImageIO.read(f)));
            panelKlawiszy.add(klawisz);
        }
        panelKlawiszy.setBounds(0, 170, 800, 600);
        add(panelKlawiszy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        File serce = new File("serce.png");
        for (int i = 0; i < zycia; i++) {
            try{
                g.drawImage(ImageIO.read(serce), (i*30) + (i+1), 10,20,20,null);
            } catch (Exception e){}
        }
        g.setFont(new Font("Arial", Font.BOLD,20));
        g.drawString("Punkty: " + punkty,570,30);

        for (int i = 0; i < listaSkladnikow.size(); i++) {
            try {
                g.drawImage(ImageIO.read(new File("Skladniki/" + listaSkladnikow.get(i) + ".png")),
                        50 * i, 120, 50, 50, null);
            } catch (Exception e){}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(750, 750);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Kreator Sushi");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        window.add(new Main());

        window.pack();
    }
}

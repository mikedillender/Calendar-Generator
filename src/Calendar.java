import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Calendar extends Applet implements Runnable, KeyListener {

    float pw=8f, ph=11f;
    private int WIDTH=1480, HEIGHT=1480;
    int defaultFont=20;
    private Thread thread;
    Graphics gfx;
    Image img;
    Color background=new Color(255, 255, 255);
    Color noSchoolCol=new Color(217, 219, 255);
    Color gridColor=new Color(0,0,0);
    int borderSize=40;
    String[] days=new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    int[] daysPerMonth=new int[]{ 31  , 28  , 31  , 30  , 31  , 30   , 31   , 31  , 30   , 31  , 30  , 31  };
    String[] months=new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    int adj=4;
    int start=getDayofYear(23,8);
    int end=getDayofYear(26,12);
    int rows=(int)(Math.ceil((end-start)/7.0));
    int currentDay=start;
    ArrayList<String>[] events;
    boolean vert=false;
    boolean[] noSchool;
    Color[] colors;
    Color[] catcolors= new Color[]{
            new Color(0,0,0),
            new Color(82, 92, 170),
            new Color(138, 24, 22),
            new Color(91, 146, 86),
            new Color(92, 29, 84),
            new Color(161, 102, 0)
    };

    public void init(){//STARTS THE PROGRAM
        HEIGHT=(int)(WIDTH*((vert)?(pw/ph):(ph/pw)));
        WIDTH=(WIDTH/7);
        WIDTH=(WIDTH*7);
        HEIGHT=(HEIGHT/rows);
        HEIGHT=(HEIGHT*rows)+borderSize;

        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        gfx.setFont(gfx.getFont().deriveFont(defaultFont));
        events=new ArrayList[end-start+1];
        noSchool =new boolean[end-start+1];
        colors=new Color[end-start+1];
        setHasSchool();
        setColors();
        addEvents();
        setCurrentDay();
        thread=new Thread(this);
        thread.start();
    }

    public void setCurrentDay(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate localDate = LocalDate.now();
        int month= Integer.parseInt( DateTimeFormatter.ofPattern("MM").format(localDate));
        int day=Integer.parseInt(DateTimeFormatter.ofPattern("dd").format(localDate));
        System.out.println(day+", "+month);
        currentDay=getDayofYear(day,month);

    }
    //           ADD EVENTS TO THE CALENDAR
    public void addEvents(){
        addWeeklyEvents();

        addEvent("301 Midterm", getDayofYear(21,10), 5);
        addEvent("301 Final", getDayofYear(17,12), 5);
        add320Q();
        add311Q();
        addEvent("320 PSet 1", getDayofYear(13,9), 2);
        addEvent("320 Exam 1", getDayofYear(11,10), 2);
        addEvent("320 Exam 2", getDayofYear(17,11), 2);
        addEvent("320 Final", getDayofYear(14,12), 2);

        addEvent("311 Exam 1", getDayofYear(8,10), 3);
        addEvent("311 Exam 2", getDayofYear(11,11), 3);
        addEvent("311 Final", getDayofYear(20,12), 3);

        addEvent("311 L0 Quiz", getDayofYear(1,9), 3);
        addEvent("311 L1 Quiz", getDayofYear(2,9), 3);
        addEvent("311 L2 Quiz", getDayofYear(7,9), 3);
        addEvent("311 L3 Quiz", getDayofYear(9,9), 3);
        addEvent("311 L4 Quiz", getDayofYear(14,9), 3);
        addEvent("311 L5 Quiz", getDayofYear(17,9), 3);
        addEvent("311 L6 Quiz", getDayofYear(21,9), 3);
        addEvent("311 L7 Quiz", getDayofYear(23,9), 3);
        addEvent("311 L8 Quiz", getDayofYear(28,9), 3);
        addEvent("311 L9 Quiz", getDayofYear(30,9), 3);
        addEvent("311 L10 Quiz", getDayofYear(5,10), 3);
        addEvent("311 L11 Quiz", getDayofYear(11,10), 3);
        addEvent("311 L12 Quiz", getDayofYear(13,10), 3);
        addEvent("311 L13 Quiz", getDayofYear(17,10), 3);
        addEvent("311 L14 Quiz", getDayofYear(19,10), 3);
        addEvent("311 L16 Quiz", getDayofYear(26,10), 3);
        addEvent("311 L17 Quiz", getDayofYear(28,10), 3);
        addEvent("311 L18 Quiz", getDayofYear(4,11), 3);
        addEvent("311 L19 Quiz", getDayofYear(9,11), 3);
        addEvent("311 L20 Quiz", getDayofYear(12,11), 3);
        addEvent("311 L21 Quiz", getDayofYear(17,11), 3);
        addEvent("311 L22 Quiz", getDayofYear(18,11), 3);
        addEvent("311 L23 Quiz", getDayofYear(1,12), 3);
        addEvent("311 L24 Quiz", getDayofYear(3,12), 3);

        addEvent("C-Catalog Released ", getDayofYear(18,10), 4);
        addEvent("Backpack Opens", getDayofYear(8,11), 4);
        addEvent("Registration Begins", getDayofYear(15,11), 4);
        addEvent("Somin Lee Interview", getDayofYear(1,9), 4);
        addEvent("Somin Lee Interview", getDayofYear(8,9), 4);
        addEvent("MASA Meeting 1", getDayofYear(8,9), 4);
        addEvent("MASA Meeting 2", getDayofYear(9,9), 4);

    }



    private void add320Q(){
        String table="Sept. 8 Semiconductor models and carriers 2.1-2.4\n" +
                "Sept. 13 Semiconductor under equilibrium 2.4, 2.5\n" +
                "Sept. 15 Equilibrium carrier distribution 2.5\n" +
                "Sept. 20 Drift and diffusion 3.1, 3.2\n" +
                "Sept. 22 Recombination-generation 3.3\n" +
                "Sept. 27 Equations of state 3.4.1-3.4.3, 3.5\n" +
                "Sept. 29 Minority carrier diffusion equation: problem solving 3.4.4\n" +
                "Oct. 4 Diode electrostatics 5.1\n" +
                "Oct. 6 Review\n" +
                "Oct. 11 Exam I\n" +
                "Oct. 13 Diode electrostatics: quantitative analysis 5.2\n" +
                "Oct. 20 The ideal diode equation 6.1\n" +
                "Oct. 25 Deviation from ideal diodes 6.2\n" +
                "Oct. 27 Optoelectronic devices, and metal-semiconductor junctions 9, 14\n" +
                "Nov. 1 MOS fundamentals I 16.1, 16.2\n" +
                "Nov. 3 MOS fundamentals II 16.3, 16.4\n" +
                "Nov. 8 MOSFET I 17.1, 17.2\n" +
                "Nov. 10 MOSFET II 17.2, 17.3\n" +
                "Nov. 15 Review\n" +
                "Nov. 17 Exam\n" +
                "Nov. 22 MOSFET non-ideal 18\n" +
                "Nov. 29 MOSFET non-ideal 18\n" +
                "Dec. 1 BJT I 10\n" +
                "Dec. 6 BJT II 11\n" +
                "Dec. 8 Conclusion\n";
        while (table.contains("\n")){
            String line=table.substring(0,table.indexOf("\n"));
            table=table.substring(table.indexOf("\n")+1);
            for (int i=0; i<12; i++){
                if (line.substring(0,months[i].length()).equals(months[i])){
                    line=line.substring(months[i].length()+2);
                    if (line.charAt(0)==' '){ line=line.substring(1); }
                    int date=Integer.parseInt(line.substring(0,line.indexOf(" ")));
                    line=line.substring(line.indexOf(" "));
                    while (line.length()>0&& (line.charAt(0)<48||line.charAt(0)>57)){
                        line=line.substring(1);
                    }
                    if (line.length()==0){i=12;continue;}
                    addEvent("320 R&Q "+line, getDayofYear(date-1,i+1), 2);
                    i=12;
                }
            }

        }
    }
    private void add311Q(){
        String table="09/9\tPS1: Basics\n" +
                "09/14\tLab 0 Report due\n" +
                "09/16\tPS2: Real opamps\n" +
                "09/21\tLab 1 Pre-lab due\n" +
                "09/23\tPS3: f(w), ps, zs\n" +
                "09/28\tLab 1 Report due\n" +
                "09/30\tPS4: Filters\n" +
                "10/5\tLab 2 Exercise Rep due\n" +
                "10/5\tLab 2 Pre-lab due\n" +
                "10/7\tPS5: Diodes\n" +
                "10/12\tLab 2 report due\n" +
                "10/14\tPS6: BJT basics\n" +
                "10/26\tLab 3 Ex Report due\n" +
                "10/26\tLab 3 Pre-lab due\n" +
                "11/2\tLab 3 report due\n" +
                "11/4\tPS7: CE Amps, biasing\n" +
                "11/9\tLab 4 Ex Report due\n" +
                "11/9\tLab 4 pre-lab due\n" +
                "11/11\tPS8: Freq. response\n" +
                "11/23\tLab 4 report due\n" +
                "12/2\tPS9: BJT amps\n" +
                "12/9\tPS10: MOS circuits\n" +
                "12/10\tLab 5 Report due\n"+
                "End";
        while (table.contains("\n")){
            String line=table.substring(0,table.indexOf("\n"));
            table=table.substring(table.indexOf("\n")+1);
            String date=line.substring(0,line.indexOf("\t"));
            line=line.substring(line.indexOf("\t")+1);

            int month=Integer.parseInt(date.substring(0,2));//NOTE: THIS IS LAZY
            int day=Integer.parseInt(date.substring(3));//NOTE: THIS IS LAZY
            System.out.println(month+" & "+day);
            int doy=getDayofYear(day,month);
            if (line.contains("PS")){doy--;}
            addEvent("311 "+line, doy, 3);

        }
    }

    private void addWeeklyEvents(){
        int fdos=getDayofYear(30,8);
        int ldos=getDayofYear(10,12);
        int secondweek=getDayofYear(12,9);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (!(i<fdos || i>ldos)) {
                if (i>secondweek){
                    if (day == 2) {
                        addEvent("301 HW Due", i, 5);
                    }
                }

            }

        }
    }

    private void setHasSchool(){
        int fdos=getDayofYear(30,8);
        int ldos=getDayofYear(10,12);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==5||day==6){
                noSchool[i-start]=true;
            }if (i<fdos || i>ldos){
                noSchool[i-start]=true;
            }
        }
        noSchool[getDayofYear(6,9)-start]=true;
        noSchool[getDayofYear(18,10)-start]=true;
        noSchool[getDayofYear(19,10)-start]=true;
        for (int i=23; i<28; i++)
            noSchool[getDayofYear(i,11)-start]=true;

    }



    public void setColors(){
        for (int i=start; i<=end; i++){
            if (noSchool[i-start]){
                colors[i-start]=noSchoolCol;
            }
        }
    }

    public void create(){
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        paintGrid(gfx);
        exportImg();
    }

    public void paint(Graphics g){
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        paintGrid(gfx);
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ paint(g); }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
        //repaint();//UPDATES FRAME
        try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    private int getDayofMonth(int dayOfYear){
        int day=dayOfYear;
        int month=0;
        for (int i=0; i<12; i++){
            if (day-daysPerMonth[i]>0){
                month=i;
                day-=daysPerMonth[i];
            }else {
                month=i;
                break;
            }
        }
        System.out.println(dayOfYear+" is the "+day+"th of "+months[month]);
        return day;
    }

    private String getDay(int dayOfYear){
        int day=dayOfYear;
        int month=0;
        for (int i=0; i<24; i++){
            int j=i;
            if(j>11){j-=12;}
            int dpm=daysPerMonth[j];
            if (day-dpm>0){
                month=i;
                day-=dpm;
            }else {
                month=j;
                break;
            }
        }
        //if (month>12){month-=12;}
        String i=months[month]+" "+day;
        return i;
    }

    public void addEvent(String e, int month, int dayofMonth, int type){
        int day=getDayofYear(dayofMonth,month);
        if (day<0||day>end){return;}
        int i=day-start;
        if (i<0||i>events.length){return;}
        e=type+" "+ e;
        if (events[i]==null){
            events[i]=new ArrayList<>();
        }
        events[i].add(e);
    }

    public void addEvent(String e, int day, int type){
        int i=day-start;
        if (day<0||day>end||i<0){return;}
        e=type+" "+ e;
        if (events[i]==null){
            events[i]=new ArrayList<>();
        }
        events[i].add(e);
    }

    public int getDayofYear(int dayOfMonth, int month){
        int d=0;
        for (int i=0; i<month-1; i++) {
            if (i >= 12) {
                d += daysPerMonth[i-12];
                if (i==14){
                    d++;
                }
            } else {
                d += daysPerMonth[i];
            }
        }
        d+=dayOfMonth;
        return d;
    }

    //INPUT
    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {
        create();
    }
    public void keyTyped(KeyEvent e) { }

    public void exportImg(){
        //String export="B:\\Libraries\\Programming\\Calender\\Calendar-Generator\\calendarImgs\\t.png";
        String export="C:\\Users\\Mike\\Documents\\GitHub\\Calendar-Generator\\calendarImgs\\t.png";
        //String export="C:\\Users\\dillemic000\\Documents\\GitHub\\Calendar-Generator\\t.png";

        RenderedImage rendImage = toBufferedImage(img);
        File file = new File(export);
        try {
            ImageIO.write(rendImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    private int getDayOfWeek(int day){
        int adj=(this.adj+start)%7-1;
        if (adj>7){
            adj-=7;
        }else if (adj<0){
            adj+=7;
        }
        int x=(day-start+adj)%7;
        return x;
    }

    public void paintGrid(Graphics gfx){
        gfx.setColor(gridColor);
        gfx.setFont(gfx.getFont().deriveFont(defaultFont*1f));

        int fontsize=gfx.getFont().getSize();

        int adj=(this.adj+start)%7-1;
        if (adj>7){
            adj-=7;
        }else if (adj<0){
            adj+=7;
        }

        for (int i=start; i<=end; i++){
            if (colors[i-start]!=null) {
                int x = (i - start + adj) % 7;
                int y = ((i + adj - start) / 7);
                int x1 = WIDTH / 7 * (x);
                int y1 = borderSize + ((HEIGHT - borderSize) / rows) * y;
                gfx.setColor(colors[i-start]);
                gfx.fillRect(x1, y1, WIDTH / 7, (HEIGHT - borderSize) / rows);

            }
        }
        Color bord=new Color(197, 202, 215);
        gfx.setColor(bord);
        gfx.fillRect(0, 0, WIDTH, borderSize);
        gfx.setColor(Color.black);

        gfx.fillRect(0, borderSize, WIDTH, 3);
        gfx.fillRect(0, 0, WIDTH, 3);
        gfx.fillRect(0, HEIGHT-3, WIDTH, 3);
        gfx.fillRect(0, 0, 3, HEIGHT);
        gfx.fillRect(WIDTH-3, 0, 3, HEIGHT);
        for (int i=1; i<8; i++){
            int x1=WIDTH/7*(i-1);

            gfx.setColor(Color.BLACK);
            gfx.drawString("" + days[i - 1], x1+10, fontsize+10);

            gfx.setColor(Color.BLACK);
            if (i<7) {
                int x = WIDTH / 7 * i ;
                gfx.fillRect(x, 0, 3, HEIGHT);
            }
        }

        for (int r=1; r<rows; r++){
            int y=borderSize+((HEIGHT-borderSize)/rows)*r;
            gfx.fillRect(0, y, WIDTH, 3);
        }

        gfx.setColor(Color.BLACK);
        for (int i=start; i<=end; i++){
            if (i<currentDay){

                int x = (i - start + adj) % 7;
                int y = ((i + adj - start) / 7);
                int x1 = WIDTH / 7 * (x);
                int y1 = borderSize + ((HEIGHT - borderSize) / rows) * y;
                int x2=x1+(WIDTH/7);
                int y2=y1+((HEIGHT - borderSize) / rows);
                gfx.drawLine(x1,y1,x2,y2);
                gfx.drawLine(x2,y1,x1,y2);
                //gfx.fillRect(x1, y1, WIDTH / 7, (HEIGHT - borderSize) / rows);
            }
        }


        int maxstrlen=(int)(WIDTH/7.0)-10;

        for (int i=start; i<=end; i++){
            gfx.setColor(catcolors[0]);
            int x=(i-start+adj)%7;
            int y=((i+adj-start)/7);
            int x1=WIDTH/7*(x)+7;
            int y1=fontsize+5+borderSize+((HEIGHT-borderSize)/rows)*y;
            gfx.drawString(getDay(i), x1, y1);
            if (events[i-start]!=null){
                for (int e=0; e<events[i-start].size(); e++){
                    int c=0;
                    String text=events[i-start].get(e).substring(2);
                    char ctext=events[i-start].get(e).charAt(0);
                    try {
                        c= Integer.parseInt(String.valueOf(ctext));
                    } catch (NumberFormatException q) {}

                    //Graphics2D g2 = (Graphics2D) gfx;
                    //g2.drawString(text+"<p color=\"#00FF00\">NOTICE</p>", x, y);
                    if (c<catcolors.length){
                        gfx.setColor(catcolors[c]);
                    }else {
                        gfx.setColor(catcolors[0]);
                    }

                    if (gfx.getFontMetrics().stringWidth(text)>maxstrlen){
                        float fm=.99f;
                        gfx.setFont(gfx.getFont().deriveFont(defaultFont*(fm)));
                        while(gfx.getFontMetrics().stringWidth(text)>maxstrlen){
                            fm-=.01f;
                            gfx.setFont(gfx.getFont().deriveFont(defaultFont*(fm)));
                        }
                        gfx.drawString(text, x1, y1 + ((e + 1) * (fontsize+1)));
                        gfx.setFont(gfx.getFont().deriveFont(defaultFont*1f));
                    }else {
                        gfx.drawString(text, x1, y1 + ((e + 1) * (fontsize+1)));
                    }

                }
            }


        }
    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) { return (BufferedImage) img; }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
}
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
    int adj=1;
    int start=getDayofYear(31,8);
    int end=getDayofYear(14,12);
    int rows=(int)(Math.ceil((end-start)/7.0));
    int currentDay=start;
    ArrayList<String>[] events;
    boolean vert=false;
    boolean[] noSchool;
    Color[] colors;
    Color[] catcolors= new Color[]{
            new Color(0,0,0),
            new Color(82, 92, 170),
            new Color(138, 53, 49),
            new Color(91, 146, 86),
            new Color(92, 29, 84)
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
        addEvent("Engr Essay", getDayofYear(8,9), 1);
        addEvent("Engr Essay", getDayofYear(24,9), 1);
        addEvent("Engr PreLab 3", getDayofYear(24,9), 1);
        addEvent("Engr PostLab 2", getDayofYear(24,9), 1);
        addEvent("215 HW", getDayofYear(12,9), 1);
        addEvent("215 HW 2", getDayofYear(19,9), 1);
        addEvent("215 HW 3", getDayofYear(26,9), 1);
        addEvent("215 HW 4", getDayofYear(3,10), 1);
        addEvent("HW 4 Analysis", getDayofYear(5,10), 1);
        addEvent("215 HW 5", getDayofYear(10,10), 1);
        addEvent("215 HW 6", getDayofYear(17,10), 1);
        addEvent("215 HW 7", getDayofYear(24,10), 1);
        addEvent("215 HW 8", getDayofYear(31,10), 1);
        addEvent("215 Lab 1", getDayofYear(16,9), 1);
        addEvent("215 Lab 2", getDayofYear(23,9), 1);
        addEvent("215 Lab 3", getDayofYear(30,9), 1);
        addEvent("215 Lab 4", getDayofYear(7,10), 1);
        addEvent("215 Quiz 1", getDayofYear(16,9), 1);
        addEvent("215 Quiz 2", getDayofYear(5,10), 1);
        addEvent("215 Quiz Due", getDayofYear(7,10), 1);
        addEvent("215 Quiz 3", getDayofYear(26,10), 1);
        addEvent("215 Quiz 4", getDayofYear(16,11), 1);
        addEvent("216 Quiz 3", getDayofYear(18,9), 4);
        addEvent("216 Quiz 4", getDayofYear(25,9), 4);
        addEvent("216 Quiz 5", getDayofYear(2,10), 4);
        addEvent("216 Quiz 6", getDayofYear(9,10), 4);
        addEvent("M216 HW", getDayofYear(16,9), 4);
        addEvent("M216 WebWork 0", getDayofYear(11,9), 4);
        addEvent("M216 WebWork 1", getDayofYear(11,9), 4);
        addEvent("M216 Lab Writeup", getDayofYear(22,9), 4);
        addEvent("M216 Lab 2 Prelab", getDayofYear(22,9), 4);
        addEvent("M216 Peer-Review", getDayofYear(25,9), 4);
        addEvent("M216 Lab 2 Final", getDayofYear(6,10), 4);
        addEvent("M216 PreLab 3", getDayofYear(6,10), 4);
        addEvent("M216 Written 2", getDayofYear(30,9), 4);
        addEvent("M216 Lab Final", getDayofYear(29,9), 4);
        addEvent("M216 WebWork 2", getDayofYear(18,9), 4);
        addEvent("M216 WebWork 3", getDayofYear(25,9), 4);
        addEvent("M216 WebWork 4", getDayofYear(2,10), 4);
        addEvent("M216 Written 3", getDayofYear(14,10), 4);
        addEvent("M216 Midterm", getDayofYear(15,10), 4);
        addEvent("M216 WebWork 5", getDayofYear(16,10), 4);
        addEvent("M216 WebWork 6", getDayofYear(23,10), 4);
        addEvent("M216 WebWork 7", getDayofYear(30,10), 4);
        addEvent("M216 WebWork 8", getDayofYear(6,11), 4);
        addEvent("M216 WebWork 9", getDayofYear(13,11), 4);
        addEvent("M216 WebWork 10", getDayofYear(20,11), 4);
        addEvent("M216 Final", getDayofYear(10,12), 4);
        addEvent("100 Lab 1", getDayofYear(11,9), 1);
        addEvent("100 In/Post 4", getDayofYear(8,10), 1);
        addEvent("100 Proj Proposal", getDayofYear(8,10), 1);
        addEvent("100 Team Docs", getDayofYear(12,11), 1);
        addEvent("200 Lab 1", getDayofYear(14,9), 2);
        addEvent("200 Lab 2", getDayofYear(21,9), 2);
        addEvent("200 Lab 3", getDayofYear(28,9), 2);
        addEvent("200 Lab 4", getDayofYear(5,10), 2);
        addEvent("200 Lab 5", getDayofYear(12,10), 2);
        addEvent("200 Lab 6", getDayofYear(19,10), 2);
        addEvent("200 Lab 7", getDayofYear(26,10), 2);
        addEvent("200 Lab 8", getDayofYear(2,11), 2);
        addEvent("200 Plan", getDayofYear(9,11), 2);
        addEvent("280 Proj 1", getDayofYear(14,9), 1);
        addEvent("280 Lab 1", getDayofYear(13,9), 1);
        addEvent("280 Lab 2", getDayofYear(27,9), 1);
        addEvent("280 Midterm", getDayofYear(26,10), 1);
        addEvent("280 Proj 2", getDayofYear(29,9), 1);
        addEvent("280 Proj 3", getDayofYear(16,10), 1);
        addEvent("280 Proj 4", getDayofYear(11,11), 1);
        addEvent("280 Proj 5", getDayofYear(4,12), 1);
    }



    private void addWeeklyEvents(){
        int lastSciOly=getDayofYear(1,5);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (!noSchool[i-start]) {
                if (day == 3&& i<lastSciOly) {
                    addEvent("Science Olympiad", i, 2);
                }
            }
        }
    }

    private void setHasSchool(){
        int lastday=getDayofYear(24,5);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==5||day==6){
                noSchool[i-start]=true;
            }
        }
    }

    private void addPowerMondays(){
        int lastMonday=getDayofYear(8,4);
        for (int i=getDayofYear(19,8); i<=end; i++){
            if(i-start<0){continue;}
            int day=getDayOfWeek(i);
            if (day==0&&!noSchool[i-start]&&i!=getDayofYear(7,10)){
                addEvent("Power Monday", i, 2);
            }
        }
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
            int dpm=daysPerMonth[j];if (i==13){dpm=dpm+1;}
            if (day-dpm>0){
                month=i;
                day-=dpm;
            }else {
                month=j;
                break;
            }
        }
        if (month>12){month-=12;}
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
        month+=12;
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
        gfx.setColor(Color.black);

        gfx.fillRect(0, borderSize, WIDTH, 3);
        gfx.fillRect(0, 0, WIDTH, 3);
        gfx.fillRect(0, HEIGHT-3, WIDTH, 3);
        gfx.fillRect(0, 0, 3, HEIGHT);
        gfx.fillRect(WIDTH-3, 0, 3, HEIGHT);

        for (int i=1; i<8; i++){
            int x1=WIDTH/7*(i-1);
            gfx.drawString("" + days[i - 1], x1+10, fontsize+10);

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



        for (int i=start; i<=end; i++){
            gfx.setColor(catcolors[0]);
            int x=(i-start+adj)%7;
            int y=((i+adj-start)/7);
            int x1=WIDTH/7*(x)+10;
            int y1=fontsize+10+borderSize+((HEIGHT-borderSize)/rows)*y;
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

                    if (text.length()>16){
                        gfx.setFont(gfx.getFont().deriveFont(defaultFont*.9f));
                        gfx.drawString(text, x1, y1 + ((e + 1) * (fontsize + 5)));
                        gfx.setFont(gfx.getFont().deriveFont(defaultFont));
                    }else {
                        gfx.drawString(text, x1, y1 + ((e + 1) * (fontsize + 5)));
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
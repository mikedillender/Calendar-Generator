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

    float pw=8.5f, ph=11f;
    private int WIDTH=1480, HEIGHT=1480;
    int defaultFont=20;
    private Thread thread;
    Graphics gfx;
    Image img;
    Color background=new Color(255, 255, 255);
    Color noSchoolCol=new Color(229, 234, 255);
    Color gridColor=new Color(0,0,0);
    int borderSize=40;
    String[] days=new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    int[] daysPerMonth=new int[]{ 31  , 28  , 31  , 30  , 31  , 30   , 31   , 31  , 30   , 31  , 30  , 31  };
    String[] months=new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    int adj=4;
    int start=getDayofYear(3,5);
    int end=getDayofYear(25,7);
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
        setHasSchool();
        addEvent("Project 1 Due", getDayofYear(11,5), 1);
        addEvent("Project 2 Due", getDayofYear(24,5), 1);
        addEvent("Project 3 Due", getDayofYear(8,6), 1);
        addEvent("Project 4 Due", getDayofYear(21,6), 1);

        addEvent("Lab 1 Due", getDayofYear(14,5), 2);
        addEvent("Lab 2 Due", getDayofYear(21,5), 2);
        addEvent("Lab 3 Due", getDayofYear(21,5), 2);
        addEvent("Lab 4 Due", getDayofYear(25,5), 2);
        addEvent("Lab 5 Due", getDayofYear(25,5), 2);
        addEvent("Lab 7 Due", getDayofYear(11,6), 2);
        addEvent("Lab 8 Due", getDayofYear(18,6), 2);
        addEvent("Lab 9 Due", getDayofYear(18,6), 2);
        addEvent("Lab 10 Due", getDayofYear(22,6), 2);

        addEvent("Written 1 Due", getDayofYear(6,5), 3);
        addEvent("Written 2 Due", getDayofYear(11,5), 3);
        addEvent("Written 3 Due", getDayofYear(13,5), 3);
        addEvent("Written 4 Due", getDayofYear(18,5), 3);
        addEvent("Written 5 Due", getDayofYear(20,5), 3);
        addEvent("Written 6 Due", getDayofYear(1,6), 3);
        addEvent("Written 7 Due", getDayofYear(3,6), 3);
        addEvent("Written 8 Due", getDayofYear(8,6), 3);
        addEvent("Written 9 Due", getDayofYear(10,6), 3);
        addEvent("Written 10 Due", getDayofYear(15,6), 3);


        addEvent("Midterm", getDayofYear(26,5), 1);
        addEvent("Final", getDayofYear(25,6), 1);

    }



    private void addWeeklyEvents(){
        int lastSciOly=getDayofYear(1,5);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (!noSchool[i-start]) {
                if (day == 3&& i<lastSciOly) {
                }
            }
        }
    }

    private void setHasSchool(){
        int firstday=getDayofYear(26,6);//of summer
        int lastday=getDayofYear(27,8);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==5||day==6){
                noSchool[i-start]=true;
            }if (firstday<=i&&i<lastday){
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
        String export="B:\\Libraries\\Programming\\Calender\\Calendar-Generator\\calendarImgs\\t.png";
        //String export="C:\\Users\\Mike\\Documents\\GitHub\\Calendar-Generator\\calendarImgs\\t.png";
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
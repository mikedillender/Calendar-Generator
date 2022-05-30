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
    private int WIDTH=1080, HEIGHT=1080;
    int defaultFont=14;
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
    int adj=5;
    int start=getDayofYear(2,5);
    int end=getDayofYear(4,9);
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
            new Color(21, 106, 36),
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
        addEvent("BPG Report Due",getDayofYear(24,5),0);
        addEvent("Deotare Meeting",getDayofYear(11,5),2);
        add428();

    }


    private void add428(){
        int first=getDayofYear(30,5);
        int last=getDayofYear(27,8);
        int diff=last-first; float ch=18-2;
        addEvent("428 Ch 3 Reading",first+5,4);
        addEvent("428 Ch 4,5 Reading",first+5+7,4);
        addEvent("428 Ch 6 Reading",first+5+7+3,4);
        addEvent("428 Ch 7 Reading",first+5+7*2+3,4);
        addEvent("428 Ch 8 Reading",first+5+7*3,4);
        addEvent("428 Ch 9 Reading",first+5+7*4,4);
        addEvent("428 Ch 10 Reading",first+5+7*5,4);
        addEvent("428 Ch 11,12 Reading",first+5+7*6,4);
        addEvent("428 Ch 13,14 Reading",first+5+7*7,4);
        addEvent("428 Ch 15 Reading",first+5+7*8,4);
        addEvent("428 Ch 16 Reading",first+5+7*8+3,4);
        addEvent("428 Ch 17 Reading",first+5+7*9+3,4);
        addEvent("428 Ch 18 Reading",first+5+7*10,4);

        System.out.println(diff);
    }

    private void addWeeklyEvents(){
        int fdos=getDayofYear(5,1);
        int ldos=getDayofYear(19,4);
        int secondweek=fdos+7;
        int ldobpg=getDayofYear(30,5);
        int fdoexp=getDayofYear(30-7,5);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            /*if (!(i<fdos || i>ldos)) {
                /*if (i>secondweek){
                    if (day == 2) {
                        //addEvent("301 HW Due", i, 5);
                    }
                }

            }*/
            if (i<ldobpg+30) {
                if (day == 4) {
                    addEvent("BPG Results Call", i, 0);
                }
            }
            if(i>fdoexp){
                if (day == 2) {
                    addEvent("ExP Meeting", i, 0);
                }
            }
        }
    }

    private void setHasSchool(){
        int fdos=getDayofYear(29,8);
        int ldos=getDayofYear(9,12);
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==5||day==6){
                noSchool[i-start]=true;
            }if (i<fdos || i>ldos){
                noSchool[i-start]=true;
            }
        }
        //noSchool[getDayofYear(6,9)-start]=true;
        //noSchool[getDayofYear(18,10)-start]=true;
        //noSchool[getDayofYear(19,10)-start]=true;
        //noSchool[getDayofYear(17,1)-start]=true;
        //for (int i=0; i<7; i++)
        //   noSchool[getDayofYear(26,2)+i-start]=true;

    }



    public void setColors(){
        int homes=getDayofYear(1,5);
        int homee=getDayofYear(9,5);
        Color homec=new Color(200,250,200);
        for (int i=start; i<=end; i++){
            if (noSchool[i-start]){
                colors[i-start]=noSchoolCol;
            }
            if (i<=homee&&i>=homes){
                colors[i-start]=homec;
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
        //String export="C:\\Users\\Mike\\Documents\\GitHub\\Calendar-Generator\\calendarImgs\\t.png";
        String export="C:\\Users\\dille\\Documents\\Calendar-Generator\\calendarImgs\\t.png";
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

        gfx.fillRect(0, borderSize, WIDTH, 2);
        gfx.fillRect(0, 0, WIDTH, 2);
        gfx.fillRect(0, HEIGHT-3, WIDTH, 2);
        gfx.fillRect(0, 0, 2, HEIGHT);
        gfx.fillRect(WIDTH-3, 0, 2, HEIGHT);
        for (int i=1; i<8; i++){
            int x1=WIDTH/7*(i-1);

            gfx.setColor(Color.BLACK);
            gfx.drawString("" + days[i - 1], x1+10, fontsize+10);

            gfx.setColor(Color.BLACK);
            if (i<7) {
                int x = WIDTH / 7 * i ;
                gfx.fillRect(x, 0, 2, HEIGHT);
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
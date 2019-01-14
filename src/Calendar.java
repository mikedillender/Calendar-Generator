import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Calendar extends Applet implements Runnable, KeyListener {

    float pw=8.4f, ph=11f;
    private int WIDTH=1280, HEIGHT=1280;
    private Thread thread;
    Graphics gfx;
    Image img;
    Color background=new Color(255, 255, 255);
    Color noSchoolCol=new Color(210, 210, 210);
    Color gridColor=new Color(0,0,0);
    int borderSize=40;
    String[] days=new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday",};
    int[] daysPerMonth=new int[]{ 31  , 28  , 31  , 30  , 31  , 30   , 31   , 31  , 30   , 31  , 30  , 31  };
    String[] months=new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    int adj=2;
    int start=getDayofYear(13,1);
    int end=getDayofYear(15,3);
    int rows=(int)(Math.ceil((end-start)/7.0));
    ArrayList<String>[] events;
    boolean vert=false;
    boolean[] noSchool;
    Color[] catcolors= new Color[]{
            new Color(0,0,0),
            new Color(82, 92, 170),
            new Color(138, 53, 49)
    };


    //           ADD EVENTS TO THE CALENDAR
    public void addEvents(){
        addEvent("Whitebook",1,14, 1);
        addEvent("Socratic Seminar",1,16, 1);
        addEvent("My Birthday",1,31, 1);
        addEvent("Rand Vocab Test",2,4, 1);
        addEvent("Start Essay",2,14, 1);
        addEvent("Grammar Test",2,21, 1);
        addEvent("Essay Due",2,22, 1);
        addEvent("Rhet Vocab Test",3,1, 1);
        addEvent("Comp Book Due",3,1, 1);
        addEvent("Grammar Test 2",3,11, 1);
        addEvent("ACT",3,12, 1);
        addEvent("End of Q3",3,15, 1);
        addEvent("Last Day of School",5,23, 1);
        addWeeklyEvents();
    }

    private void addWeeklyEvents(){
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (!noSchool[i-start]) {
                if (day == 3) {
                    addEvent("Science Olympiad", i, 2);
                } else if (day == 5) {
                    addEvent("Comp Sci Club", i, 2);
                    addEvent("Science Bowl", i, 2);

                }
            }
        }
    }

    private void setHasSchool(){
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==0||day==6){
                noSchool[i-start]=true;
            }
        }
        for (int i=getDayofYear(18,3); i<=getDayofYear(22,3); i++){
            if (i-start>=0&&i-start<noSchool.length){
                noSchool[i-start]=true;
            }
        }
        noSchool[getDayofYear(21,1)-start]=true;
        noSchool[getDayofYear(18,2)-start]=true;
    }

    private void addPowerMondays(){
        for (int i=start; i<=end; i++){
            int day=getDayOfWeek(i);
            if (day==1&&!noSchool[i-start]){
                addEvent("Power Monday", i, 2);
            }
        }
    }

    public void init(){//STARTS THE PROGRAM
        HEIGHT=(int)(1280f*((vert)?(pw/ph):(ph/pw)));
        WIDTH=(WIDTH/7);
        WIDTH=(WIDTH*7);
        HEIGHT=(HEIGHT/rows);
        HEIGHT=(HEIGHT*rows)+borderSize;

        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        events=new ArrayList[end-start+1];
        noSchool =new boolean[end-start+1];
        setHasSchool();
        addPowerMondays();
        addEvents();
        thread=new Thread(this);
        thread.start();
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
        for (int i=0; i<12; i++){
            if (day-daysPerMonth[i]>0){
                month=i;
                day-=daysPerMonth[i];
            }else {
                month=i;
                break;
            }
        }
        String i=months[month]+" "+day;
        return i;
    }

    public void addEvent(String e, int month, int dayofMonth, int type){
        int day=getDayofYear(dayofMonth,month);
        if (day<0||day>end){return;}
        int i=day-start;
        e=type+" "+ e;
        if (events[i]==null){
            events[i]=new ArrayList<>();
        }
        events[i].add(e);
    }

    public void addEvent(String e, int day, int type){
        int i=day-start;
        if (day<0||day>end){return;}
        e=type+" "+ e;
        if (events[i]==null){
            events[i]=new ArrayList<>();
        }
        events[i].add(e);
    }

    public int getDayofYear(int dayOfMonth, int month){
        int d=0;
        for (int i=0; i<month-1; i++){
            d+=daysPerMonth[i];
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
        String export="B:\\Libraries\\Programming\\Calendar Generator\\images\\t.png";
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
        int adj=(2+start)%7-1;
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
        Font currentFont = gfx.getFont();
        Font newFont = currentFont.deriveFont(20f);
        gfx.setFont(newFont);
        int fontsize=gfx.getFont().getSize();

        int adj=(2+start)%7-1;
        if (adj>7){
            adj-=7;
        }else if (adj<0){
            adj+=7;
        }

        for (int i=start; i<=end; i++){
            int x=(i-start+adj)%7;
            int y=((i+adj-start)/7);
            int x1=WIDTH/7*(x);
            int y1=borderSize+((HEIGHT-borderSize)/rows)*y;
            gfx.setColor(noSchoolCol);
            if (noSchool[i-start]){
                gfx.fillRect(x1,y1,WIDTH/7, (HEIGHT-borderSize)/rows);
            }
            gfx.setColor(Color.black);

        }

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

                    switch (c){

                        default:
                            break;
                    }
                    //Graphics2D g2 = (Graphics2D) gfx;
                    //g2.drawString(text+"<p color=\"#00FF00\">NOTICE</p>", x, y);
                    if (c<catcolors.length){
                        gfx.setColor(catcolors[c]);
                    }else {
                        gfx.setColor(catcolors[0]);
                    }
                    gfx.drawString(text, x1, y1+((e+1)*(fontsize+5)));

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
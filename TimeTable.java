import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class TimeTable extends JFrame implements ActionListener {
    private JPanel screen = new JPanel();
    private JPanel tools = new JPanel();
    private JButton[] tool;
    private JTextField[] field;
    private CourseArray courses;
    private Color[] CRScolor;
    private static Logger logger = Logger.getLogger("TimeTableLog");
    private static final String CLASH_FILE_PATH = "lse-f-91.stu";
    private Autoassociator autoassociator;
    private int iterations, shifts;
    private boolean isLoaded = false;

    public TimeTable() {
        super("Dynamic Time Table");
        this.CRScolor = new Color[]{Color.RED, Color.GREEN, Color.BLACK};
        this.setSize(500, 800);
        this.setLayout(new FlowLayout());
        this.screen.setPreferredSize(new Dimension(400, 800));
        this.add(this.screen);
        this.setTools();
        this.add(this.tools);
        this.setVisible(true);
        initializeLogger();
    }

    private void initializeLogger() {
        try {
            FileHandler fh = new FileHandler("TimeTableLog.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTools() {
        String[] capField = new String[]{"Slots:", "Courses:", "Iters:", "Shift:"};
        this.field = new JTextField[capField.length];
        String[] capButton = new String[]{"Load", "Start", "Step", "Continue", "Print", "Exit"};
        this.tool = new JButton[capButton.length];
        this.tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));

        for (int i = 0; i < this.field.length; i++) {
            this.tools.add(new JLabel(capField[i]));
            this.field[i] = new JTextField(5);
            this.tools.add(this.field[i]);
        }

        for (int i = 0; i < this.tool.length; i++) {
            this.tool[i] = new JButton(capButton[i]);
            this.tool[i].addActionListener(this);
            this.tools.add(this.tool[i]);
        }
    }

    public void actionPerformed(ActionEvent click) {
        JButton source = (JButton) click.getSource();
        if (source.getText().equals("Load")) {
            int slots = Integer.parseInt(this.field[0].getText());
            this.courses = new CourseArray(Integer.parseInt(this.field[1].getText()) + 1, slots);
            this.courses.readClashes(CLASH_FILE_PATH);
            this.autoassociator = new Autoassociator(this.courses.length(), slots);
            this.iterations = Integer.parseInt(this.field[2].getText());
            this.shifts = Integer.parseInt(this.field[3].getText());
            this.isLoaded = true;
            this.draw();
        } else if (isLoaded && source.getText().equals("Start")) {
            for (int i = 1; i < this.courses.length(); i++) {
                this.courses.setSlot(i, 0);
            }
            automatedRun();
        } else if (isLoaded && source.getText().equals("Step")) {
            stepIteration();
        } else if (isLoaded && source.getText().equals("Continue")) {
            automatedRun();
        } else if (source.getText().equals("Print")) {
            printTimetable();
        } else if (source.getText().equals("Exit")) {
            System.exit(0);
        }
    }

    public void draw() {
        Graphics g = this.screen.getGraphics();
        g.clearRect(0, 0, this.screen.getWidth(), this.screen.getHeight());
        int width = Integer.parseInt(this.field[0].getText()) * 10;

        for (int courseIndex = 1; courseIndex < this.courses.length(); ++courseIndex) {
            int status = this.courses.status(courseIndex);
            Color drawColor = (status > 0) ? CRScolor[0] : CRScolor[1];
            g.setColor(drawColor);
            g.drawLine(0, courseIndex * 10, width, courseIndex * 10);

            g.setColor(CRScolor[2]);
            int slotPosition = 10 * this.courses.slot(courseIndex);
            g.drawLine(slotPosition, courseIndex * 10, slotPosition + 10, courseIndex * 10);
        }
    }

    private void automatedRun() {
        for (int iteration = 1; iteration <= this.iterations; iteration++) {
            this.courses.iterate(this.shifts);
            this.autoassociator.train(this.courses.getSlots());
            this.draw();
            int clashes = this.courses.clashesLeft();
            logger.info("Iteration: " + iteration + " Shifts: " + this.shifts + " Clashes: " + clashes);
        }
    }

    private void stepIteration() {
        this.courses.iterate(this.shifts);
        this.autoassociator.train(this.courses.getSlots());
        this.draw();
        int clashes = this.courses.clashesLeft();
        logger.info("Step Iteration - Shifts: " + this.shifts + " Clashes: " + clashes);
    }

    private void printTimetable() {
        System.out.println("Exam\tSlot\tClashes");
        for (int i = 1; i < this.courses.length(); i++) {
            System.out.println("" + i + "\t" + this.courses.slot(i) + "\t" + this.courses.status(i));
        }
    }

    public static void main(String[] args) {
        new TimeTable();
    }
}

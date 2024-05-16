import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CourseArray {
    private int[][] clashes;
    private int[] slots;
    private int[] status;
    private int courses;
    private int timeslots;
    private Random random;

    public CourseArray(int courses, int timeslots) {
        this.courses = courses;
        this.timeslots = timeslots;
        this.clashes = new int[courses][courses];
        this.slots = new int[courses];
        this.status = new int[courses];
        this.random = new Random();
    }

    public void readClashes(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            for (int i = 0; i < courses; i++) {
                for (int j = 0; j < courses; j++) {
                    if (scanner.hasNextInt()) {
                        clashes[i][j] = scanner.nextInt();
                    } else {
                        clashes[i][j] = 0;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setSlot(int course, int slot) {
        slots[course] = slot;
    }

    public int slot(int course) {
        return slots[course];
    }

    public int status(int course) {
        return status[course];
    }

    public int length() {
        return courses;
    }

    public void iterate(int shifts) {
        for (int i = 0; i < shifts; i++) {
            int course = random.nextInt(courses);
            int slot = random.nextInt(timeslots);
            slots[course] = slot;
            updateStatus();
        }
    }

    private void updateStatus() {
        Arrays.fill(status, 0);
        for (int i = 0; i < courses; i++) {
            for (int j = i + 1; j < courses; j++) {
                if (slots[i] == slots[j]) {
                    status[i]++;
                    status[j]++;
                }
            }
        }
    }

    public int[] getSlots() {
        return slots;
    }

    public int clashesLeft() {
        int clashes = 0;
        for (int stat : status) {
            clashes += stat;
        }
        return clashes;
    }
}

import java.util.Random;

public class Autoassociator {
    private int[][] weights;
    private int numCourses;
    private int numSlots;

    public Autoassociator(int numCourses, int numSlots) {
        this.numCourses = numCourses;
        this.numSlots = numSlots;
        this.weights = new int[numCourses][numSlots];
        initializeWeights();
    }

    private void initializeWeights() {
        Random rand = new Random();
        for (int i = 0; i < numCourses; i++) {
            for (int j = 0; j < numSlots; j++) {
                weights[i][j] = rand.nextInt(2);
            }
        }
    }

    public void train(int[] slots) {
        for (int i = 0; i < numCourses; i++) {
            for (int j = 0; j < numSlots; j++) {
                if (slots[i] == j) {
                    weights[i][j] += 1;
                }
            }
        }
    }

    public int[] getWeightsForSlot(int slot) {
        int[] slotWeights = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            slotWeights[i] = weights[i][slot];
        }
        return slotWeights;
    }
}

public class Autoassociator {
    private int[][] weights;
    private int trainingCapacity;

    public Autoassociator(int numCourses) {
        this.trainingCapacity = numCourses;
        this.weights = new int[numCourses][numCourses];
        for (int i = 0; i < numCourses; i++) {
            for (int j = 0; j < numCourses; j++) {
                if (i == j) {
                    weights[i][j] = 1; 
                } else {
                    weights[i][j] = 0;
                }
            }
        }
    }

    public int getTrainingCapacity() {
        return trainingCapacity;
    }


    public void training(int[] pattern) {
        for (int i = 0; i < trainingCapacity; i++) {
            for (int j = 0; j < trainingCapacity; j++) {
                weights[i][j] += pattern[i] * pattern[j]; 
            }
        }
    }


    public void unitUpdate(int[] neurons, int index) {
        int newState = 0;
        for (int j = 0; j < trainingCapacity; j++) {
            newState += weights[index][j] * neurons[j];
        }
        neurons[index] = newState > 0 ? 1 : -1;
    }


    public void fullUpdate(int[] neurons) {
        int[] tempState = new int[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            tempState[i] = neurons[i];
            unitUpdate(tempState, i);
        }
        System.arraycopy(tempState, 0, neurons, 0, neurons.length);
    }
}

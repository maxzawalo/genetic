package maxzawalo.genetic;

import java.util.Arrays;

public class Individual {
	private int[] genes;
	private int fitness = AlgorithmPB.NO_FITNESS;

	public Individual(int geneSize) {
		genes = new int[geneSize];
	}

	// Создаем случайную особь
	public void generateIndividual() {
		for (int i = 0; i < size(); i++) {
			int gene = AlgorithmPB.randomValue(i);
			genes[i] = gene;
		}
	}

	public int getGene(int index) {
		return genes[index];
	}

	public int[] getGene() {
		return genes;
	}

	public void setGene(int index, int value) {
		genes[index] = value;
		fitness = AlgorithmPB.NO_FITNESS;
	}

	public int size() {
		return genes.length;
	}

	public int getFitness() {
		if (fitness == AlgorithmPB.NO_FITNESS) {
			fitness = getFitness(this);
		}
		return fitness;
	}

	int getFitness(Individual individual) {
		return AlgorithmPB.tf.check(individual.getGene());
	}

	@Override
	public String toString() {
		return Arrays.toString(genes);
	}
}
package maxzawalo.genetic;

public class Population {

	Individual[] individuals;

	public Population(int geneSize, int populationSize, boolean initialise) {
		individuals = new Individual[populationSize];
		if (initialise) {
			// Создаем популяцию
			for (int i = 0; i < size(); i++) {
				Individual newIndividual = new Individual(geneSize);
				newIndividual.generateIndividual();
				saveIndividual(i, newIndividual);
			}
		}
	}

	public Individual getIndividual(int index) {
		return individuals[index];
	}

	public Individual getFittest() {
		Individual fittest = individuals[0];
		// Поиск лучшей особи
		for (int i = 0; i < size(); i++) {
			if (fittest.getFitness() <= getIndividual(i).getFitness()) {
				fittest = getIndividual(i);
			}
		}
		return fittest;
	}

	public int size() {
		return individuals.length;
	}

	public void saveIndividual(int index, Individual indiv) {
		individuals[index] = indiv;
	}
}
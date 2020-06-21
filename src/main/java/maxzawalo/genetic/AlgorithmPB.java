package maxzawalo.genetic;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Модифицированый генетический алгоритм. Процесс ведется по массивам допустимых
 * значений (категориальных). Для каждой хромосомы свой список.
 *
 */
public class AlgorithmPB {
	// Начинаем с единицы, потому что при maxFitness = 0 ген неприемлем
	public static final int MIN_FITNESS = 1;
	public static final int NO_FITNESS = -1;

	public double uniformRate = 0.5;
	public double mutationRate = 0.3;
	public boolean elita = true;
	public int geneSize = 0;
	public static int[][] posible_values;
	public static ITargetFunction tf;
	public int maxGenerationCount = 100;
	public int populationSize = 20;
	public int minFitness = 0;
	public int solutionFitness = Integer.MAX_VALUE;

	/**
	 * Эволюция
	 * 
	 * @param pop
	 * @return
	 */
	public Population evolvePopulation(Population pop) {
		Population newPopulation = new Population(geneSize, pop.size(), false);
		// Оставляем лучшую особь
		if (elita)
			newPopulation.saveIndividual(0, pop.getFittest());

		int elitaOffset = 0;
		if (elita)
			elitaOffset = 1;

		// Создаем новых особей скрещиванием
		for (int i = elitaOffset; i < pop.size(); i++) {
			Individual ind1 = tournamentSelection(pop);
			Individual ind2 = tournamentSelection(pop);
			Individual newInd = Crossover(ind1, ind2);
			newPopulation.saveIndividual(i, newInd);
		}
		for (int i = elitaOffset; i < newPopulation.size(); i++)
			Mutate(newPopulation.getIndividual(i));
		return newPopulation;
	}

	/**
	 * Скрещивание
	 * 
	 * @param ind1
	 * @param ind2
	 * @return
	 */
	private Individual Crossover(Individual ind1, Individual ind2) {
		Individual newSol = new Individual(geneSize);
		// Loop through genes
		for (int i = 0; i < ind1.size(); i++) {
			// Crossover
			if (Math.random() <= uniformRate) {
				newSol.setGene(i, ind1.getGene(i));
			} else {
				newSol.setGene(i, ind2.getGene(i));
			}
		}
		return newSol;
	}

	public static int randomValue(int pos) {
		return posible_values[pos][ThreadLocalRandom.current().nextInt(0, posible_values[pos].length)];
	}

	/**
	 * Мутация
	 * 
	 * @param indiv
	 */
	private void Mutate(Individual indiv) {
		for (int i = 0; i < indiv.size(); i++) {
			if (Math.random() <= mutationRate) {
				int gene = randomValue(i);
				indiv.setGene(i, gene);
			}
		}
	}

	/**
	 * Выбираем особь для скрещивания
	 * 
	 * @param pop
	 * @return
	 */
	private Individual tournamentSelection(Population pop) {
		int tournamentSize = populationSize / 4;
		// Создаем популяцию для турнирного отбора
		Population tournament = new Population(geneSize, tournamentSize, false);
		// Берем случайную особь для каждой позиции в турнире
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (int) (Math.random() * pop.size());
			tournament.saveIndividual(i, pop.getIndividual(randomId));
		}
		Individual fittest = tournament.getFittest();
		return fittest;
	}

	/**
	 * Запуск алгоритма
	 * 
	 * @return Решение (лучшую особь) или null если не нашел
	 */
	public Individual Run() {
		Population pop = new Population(geneSize, populationSize, true);
		int generationCount = 0;
		int maxFitness = MIN_FITNESS;

		while (generationCount < maxGenerationCount) {
			generationCount++;
			if (generationCount % 1000 == 0)
				System.out.print("Эпоха: " + generationCount + " ЦФ: " + pop.getFittest().getFitness());
			pop = evolvePopulation(pop);
			if (generationCount % 1000 == 0)
				System.out.println(pop.getFittest());
			if (pop.getFittest().getFitness() > maxFitness)
				maxFitness = pop.getFittest().getFitness();

			if (maxFitness >= solutionFitness)
				break;
		}
		System.out.println("Эпоха: " + generationCount);
		if (pop.getFittest().getFitness() < minFitness) {
			System.out.println("Нет решения.");
		} else {
			System.out.println("Решение найдено.");
			System.out.println("Гены:");
			System.out.println(pop.getFittest());
			return pop.getFittest();
		}
		return null;
	}
}
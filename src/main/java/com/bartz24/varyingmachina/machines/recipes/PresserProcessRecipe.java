package com.bartz24.varyingmachina.machines.recipes;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;

public class PresserProcessRecipe extends ProcessRecipe {
	public static class PresserPattern {
		public int[][] pattern;

		public static PresserPattern getDefaultPlatePattern() {
			return new PresserPattern(new int[][] { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } });
		}

		public static PresserPattern getDefaultRodPattern() {
			return new PresserPattern(new int[][] { { 1 }, { 1 }, { 1 }, { 1 } });
		}

		public static PresserPattern getDefaultWirePattern() {
			return new PresserPattern(new int[][] { { 1, 1, 1, 1 } });
		}

		public static PresserPattern getDefaultGearPattern() {
			return new PresserPattern(new int[][] { { 1, 0, 1, 0, 1 }, { 0, 1, 1, 1, 0 }, { 1, 1, 0, 1, 1 },
					{ 0, 1, 1, 1, 0 }, { 1, 0, 1, 0, 1 } });
		}

		public PresserPattern(int[][] pattern) {
			this.pattern = pattern;
		}

		public boolean patternMatches(PresserPattern pattern2) {
			this.cleanPattern();
			pattern2.cleanPattern();
			if (pattern.length != pattern2.pattern.length || pattern[0].length != pattern2.pattern.length)
				return false;
			for (int y = 0; y < pattern.length; y++) {
				for (int x = 0; x < pattern[0].length; x++) {
					if (pattern[y][x] != pattern2.pattern[y][x])
						return false;
				}
			}
			return true;
		}

		public void cleanPattern() {
			List<Integer> rowsToRemove = new ArrayList();
			for (int y = 0; y < pattern.length; y++) {
				boolean empty = true;
				for (int x = 0; x < pattern[y].length; x++) {
					if (pattern[y][x] == 1) {
						empty = false;
						break;
					}
				}
				if (empty)
					rowsToRemove.add(y);
			}
			removeRows(rowsToRemove);

			List<Integer> colsToRemove = new ArrayList();
			for (int x = 0; x < pattern[0].length; x++) {
				boolean empty = true;
				for (int y = 0; y < pattern.length; y++) {
					if (pattern[y][x] == 1) {
						empty = false;
						break;
					}
				}
				if (empty)
					colsToRemove.add(x);
			}
			removeCols(colsToRemove);
		}

		private void removeRows(List<Integer> rowsToRemove) {
			int[][] newPattern = new int[pattern.length - rowsToRemove.size()][pattern[0].length];
			int curNewY = 0;
			for (int y = 0; y < pattern.length; y++) {
				if (!rowsToRemove.contains(y)) {
					for (int x = 0; x < pattern[y].length; x++) {
						newPattern[curNewY][x] = pattern[y][x];
					}
					curNewY++;
				}
			}
			pattern = newPattern;
		}

		private void removeCols(List<Integer> colsToRemove) {
			int[][] newPattern = new int[pattern.length][pattern[0].length - colsToRemove.size()];
			int curNewX = 0;
			for (int x = 0; x < pattern[0].length; x++) {
				if (!colsToRemove.contains(x)) {
					for (int y = 0; y < pattern.length; y++) {
						newPattern[y][curNewX] = pattern[y][x];
					}
					curNewX++;
				}
			}
			pattern = newPattern;
		}
	}

	private PresserPattern pattern;

	public PresserProcessRecipe(List<RecipeObject> output, List<RecipeObject> input, String type,
			PresserPattern pattern, float... numVals) {
		super(output, input, type, numVals);
		this.pattern = pattern;
	}

	public PresserProcessRecipe(List<RecipeObject> input, String type, PresserPattern pattern, float... numVals) {
		super(input, type, numVals);
		this.pattern = pattern;
	}

	public boolean isInputRecipeEqualTo(ProcessRecipe recipe, boolean forceEqual) {
		return super.isInputRecipeEqualTo(recipe, forceEqual) && recipe instanceof PresserProcessRecipe
				&& new PresserPattern(pattern.pattern)
						.patternMatches(new PresserPattern(((PresserProcessRecipe) recipe).pattern.pattern));
	}

	public PresserPattern getPattern() {
		return pattern;
	}
}

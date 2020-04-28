package hu.skzs.familytree.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hu.skzs.familytree.Couple;
import hu.skzs.familytree.Member;
import hu.skzs.familytree.Person;

class GridLayout {

	private List<List<Member>> grid = new ArrayList<>();
	private int horizontalSize;

	public GridLayout(Set<Member> members) {
		horizontalSize = placeMembers(0, 0, members);
	}

	private int placeMembers(int generation, int horizontalPosition, Set<Member> members) {
		if (members.isEmpty()) {
			return 1;
		}

		int originalHorizontalPosition = horizontalPosition;
		generation++;
		for (Member member : members) {

			if (member instanceof Person) {
				placeMember(generation, ++horizontalPosition, member);

			} else if (member instanceof Couple) {
				int descendantHorizontalSize = placeMembers(generation, horizontalPosition, ((Couple) member).getDescendants());
				for (int i = 0; i < descendantHorizontalSize; i++) {
					placeMember(generation, ++horizontalPosition, member);
				}
			}
		}

		return horizontalPosition - originalHorizontalPosition;
	}

	private void placeMember(int generation, int horizontalPosition, Member member) {
		while (grid.size() < generation) {
			grid.add(new ArrayList<>());
		}

		List<Member> generationList = grid.get(generation - 1);

		while (generationList.size() < horizontalPosition - 1) {
			generationList.add(null);
		}
		generationList.add(member);
	}

	public int getNumberOfGenerations() {
		return grid.size();
	}

	public int getHorizontalSize() {
		return horizontalSize;
	}

	public GridPosition getPosition(Member member) {
		for (int generation = 0; generation < grid.size(); generation++) {
			List<Member> generationList = grid.get(generation);
			for (int horizontalPosition = 0; horizontalPosition < grid.get(generation).size(); horizontalPosition++) {
				if (member.equals(generationList.get(horizontalPosition))) {
					int horizontalSize = 1;
					while (generationList.size() > horizontalPosition + horizontalSize && member.equals(generationList.get(horizontalPosition + horizontalSize))) {
						horizontalSize++;
					}
					return new GridPosition(generation, horizontalPosition, horizontalSize);
				}
			}
		}
		throw new IllegalArgumentException("There is no such member in the grid layout: " + member);
	}
}

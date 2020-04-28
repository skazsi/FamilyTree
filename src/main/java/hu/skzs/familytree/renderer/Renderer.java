package hu.skzs.familytree.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import hu.skzs.familytree.Couple;
import hu.skzs.familytree.Member;
import hu.skzs.familytree.Person;

public class Renderer {

	@Value("${renderer.memberWidth}")
	private int memberWidth;

	@Value("${renderer.memberHeight}")
	private int memberHeight;

	@Value("${renderer.imageSize}")
	private int imageSize;

	@Value("${renderer.lineVerticalSpace}")
	private int lineVerticalSpace;

	private final ImageProvider imageProvider;

	public Renderer(ImageProvider imageProvider) {
		this.imageProvider = Objects.requireNonNull(imageProvider);
	}

	public BufferedImage renderer(Set<Member> members) throws Exception {

		GridLayout gridLayout = new GridLayout(members);


		Dimension dimension = new Dimension(memberWidth * gridLayout.getHorizontalSize(), memberHeight * gridLayout.getNumberOfGenerations());

		BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		graphics.setRenderingHints(rh);

		Font font = new Font("Arial", Font.PLAIN, 10);
		graphics.setFont(font);

		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, dimension.width + memberWidth, dimension.height + memberHeight);

		drawLines(null, members, gridLayout, graphics);
		drawMember(members, gridLayout, graphics);

		return image;
	}

	private void drawLines(Member parent, Set<Member> members, GridLayout gridLayout, Graphics2D graphics) {
		for (Member member : members) {
			if (parent != null) {

				GridPosition position = gridLayout.getPosition(member);
				int xCellCenter = position.getHorizontalPosition() * memberWidth + position.getHorizontalSize() * memberWidth / 2;
				int yCellCenter = position.getGeneration() * memberWidth + memberHeight / 2;

				GridPosition parentPosition = gridLayout.getPosition(parent);
				int xParentCellCenter = parentPosition.getHorizontalPosition() * memberWidth + parentPosition.getHorizontalSize() * memberWidth / 2;
				int yParentCellCenter = parentPosition.getGeneration() * memberWidth + memberHeight / 2;

				int lineHorizontalSpace = (int) ((double) lineVerticalSpace / (yCellCenter - yParentCellCenter) * Math.abs(xCellCenter - xParentCellCenter));

				graphics.setStroke(new BasicStroke(2));
				graphics.setColor(Color.gray);
				if (xParentCellCenter > xCellCenter) {
					graphics.drawLine(xParentCellCenter - lineHorizontalSpace, yParentCellCenter + lineVerticalSpace, xCellCenter + lineHorizontalSpace, yCellCenter - lineVerticalSpace);
				}
				else {
					graphics.drawLine(xParentCellCenter + lineHorizontalSpace, yParentCellCenter + lineVerticalSpace, xCellCenter - lineHorizontalSpace, yCellCenter - lineVerticalSpace);
				}
			}

			if (member instanceof Couple) {
				Couple couple = (Couple) member;

				if (!couple.getDescendants().isEmpty()) {
					drawLines(member, couple.getDescendants(), gridLayout, graphics);
				}
			}
		}
	}

	private void drawMember(Set<Member> members, GridLayout gridLayout, Graphics2D graphics) {
		for (Member member : members) {
			GridPosition position = gridLayout.getPosition(member);
			int xCellCenter = position.getHorizontalPosition() * memberWidth + position.getHorizontalSize() * memberWidth / 2;
			int yCellCenter = position.getGeneration() * memberWidth + memberHeight / 2;

			if (member instanceof Person) {
				Person person = (Person) member;
				graphics.setColor(Color.black);

				int nameWidth = graphics.getFontMetrics().stringWidth(person.getName());

				graphics.drawImage(imageProvider.getImage(person), xCellCenter - imageSize / 2, yCellCenter - imageSize / 2, null);
				graphics.drawString(person.getName(), xCellCenter - nameWidth / 2, yCellCenter - imageSize / 2);
			}

			else if (member instanceof Couple) {
				Couple couple = (Couple) member;
				graphics.setColor(Color.black);

				int nameWidth = graphics.getFontMetrics().stringWidth(couple.getPerson().getName() + " + " + couple.getPartner().getName());

				graphics.drawImage(imageProvider.getImage(couple.getPartner()), xCellCenter - 10, yCellCenter - imageSize / 2, null);
				graphics.drawImage(imageProvider.getImage(couple.getPerson()), xCellCenter - imageSize + 10, yCellCenter - imageSize / 2, null);
				graphics.drawString(couple.getPerson().getName() + " + " + couple.getPartner().getName(), xCellCenter - nameWidth / 2, yCellCenter - imageSize / 2);

				if (!couple.getDescendants().isEmpty()) {
					drawMember(couple.getDescendants(), gridLayout, graphics);
				}
			}
		}
	}
}

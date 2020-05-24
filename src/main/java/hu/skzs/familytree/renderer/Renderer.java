package hu.skzs.familytree.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import hu.skzs.familytree.Couple;
import hu.skzs.familytree.Member;
import hu.skzs.familytree.Person;

public class Renderer {

	@Value("${renderer.debugGrid}")
	private boolean debugGrid;

	@Value("${renderer.memberWidth}")
	private int memberWidth;

	@Value("${renderer.memberHeight}")
	private int memberHeight;

	@Value("${renderer.imageSize}")
	private int imageSize;

	@Value("${renderer.lineVerticalSpace}")
	private int lineVerticalSpace;

	@Value("${renderer.dateFormat}")
	private String dateFormat;

	private final ImageProvider imageProvider;
	private SimpleDateFormat sdf;

	private enum Align {
		LEFT, CENTER, RIGTH
	}

	public Renderer(ImageProvider imageProvider) {
		this.imageProvider = Objects.requireNonNull(imageProvider);
	}

	@PostConstruct
	public void init() {
		this.sdf = new SimpleDateFormat(dateFormat);
	}

	public BufferedImage renderer(Set<Member> members) throws Exception {

		GridLayout gridLayout = new GridLayout(members);
		Dimension dimension = new Dimension(memberWidth * gridLayout.getHorizontalSize(), memberHeight * gridLayout.getNumberOfGenerations());

		BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		graphics.setRenderingHints(renderingHints);

		drawBackground(dimension, graphics);
		drawCreated(graphics);
		drawLines(null, members, gridLayout, graphics);
		drawMember(members, gridLayout, graphics);
		return image;
	}

	private void drawBackground(Dimension dimension, Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, dimension.width + memberWidth, dimension.height + memberHeight);
	}

	private void drawCreated(Graphics2D graphics) {
		graphics.setColor(Color.lightGray);
		graphics.setFont(new Font("Arial", Font.PLAIN, 9));
		graphics.drawString("Created: " + sdf.format(new Date()), 3, 10);
	}

	private void drawLines(Member parent, Set<Member> members, GridLayout gridLayout, Graphics2D graphics) {
		for (Member member : members) {
			if (parent != null) {

				GridPosition position = gridLayout.getPosition(member);

				int xCellCenter = position.getHorizontalPosition() * memberWidth + position.getHorizontalSize() * memberWidth / 2;
				int yCellCenter = position.getGeneration() * memberHeight + memberHeight / 2;

				GridPosition parentPosition = gridLayout.getPosition(parent);
				int xParentCellCenter = parentPosition.getHorizontalPosition() * memberWidth + parentPosition.getHorizontalSize() * memberWidth / 2;
				int yParentCellCenter = parentPosition.getGeneration() * memberHeight + memberHeight / 2;

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
		graphics.setColor(Color.black);
		graphics.setFont(new Font("Arial", Font.PLAIN, 10));

		for (Member member : members) {
			GridPosition position = gridLayout.getPosition(member);
			if (debugGrid) {
				graphics.setStroke(new BasicStroke(1));
				graphics.setColor(Color.lightGray);
				graphics.drawRect(position.getHorizontalPosition() * memberWidth, position.getGeneration() * memberHeight, position.getHorizontalSize() * memberWidth, memberHeight);
			}

			int xCellCenter = position.getHorizontalPosition() * memberWidth + position.getHorizontalSize() * memberWidth / 2;
			int yCellCenter = position.getGeneration() * memberHeight + memberHeight / 2;

			if (member instanceof Person) {
				Person person = (Person) member;
				drawPerson(person, Align.CENTER, new Point(xCellCenter, yCellCenter), graphics);
			}

			else if (member instanceof Couple) {
				Couple couple = (Couple) member;
				drawPerson(couple.getPartner(), Align.LEFT, new Point(xCellCenter, yCellCenter), graphics);
				drawPerson(couple.getPerson(), Align.RIGTH, new Point(xCellCenter, yCellCenter), graphics);

				if (!couple.getDescendants().isEmpty()) {
					drawMember(couple.getDescendants(), gridLayout, graphics);
				}
			}
		}
	}

	private void drawPerson(Person person, Align align, Point cellCenter, Graphics2D graphics) {
		String lived = (person.getBirthPlace() != null ? person.getBirthPlace() + ", " : "") + sdf.format(person.getBirthDate()) + "-"
				+ (person.getDied() != null ? sdf.format(person.getDied()) : "");
		int nameWidth = graphics.getFontMetrics().stringWidth(person.getName());
		int livedWidth = graphics.getFontMetrics().stringWidth(lived);

		int xImage;
		int xName;
		int xLived;
		int separator = 10;

		if (align == Align.LEFT) {
			xImage = cellCenter.x - separator;
			xName = cellCenter.x + separator;
			xLived = xName;

		} else if (align == Align.RIGTH) {
			xImage = cellCenter.x - separator - imageSize + separator * 2;
			xName = cellCenter.x - separator - nameWidth;
			xLived = cellCenter.x - separator - livedWidth;

		} else {
			xImage = cellCenter.x - imageSize / 2;
			xName = cellCenter.x - nameWidth / 2;
			xLived = cellCenter.x - livedWidth / 2;
		}

		graphics.setColor(Color.black);
		graphics.drawImage(imageProvider.getImage(person), xImage, cellCenter.y - imageSize / 2 - separator, null);
		graphics.drawString(person.getName(), xName, cellCenter.y + imageSize / 2);
		graphics.drawString(lived, xLived, cellCenter.y + imageSize / 2 + separator);
	}
}

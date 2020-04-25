package hu.skzs.familytree.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;

import hu.skzs.familytree.Couple;
import hu.skzs.familytree.Member;
import hu.skzs.familytree.Person;

public class Renderer {

	@Value("${renderer.memberWidth}")
	private int memberWidth;

	@Value("${renderer.memberHeight}")
	private int memberHeight;

	private final ImageProvider imageProvider;

	public Renderer(ImageProvider imageProvider) {
		this.imageProvider = Objects.requireNonNull(imageProvider);
	}

	public BufferedImage renderer(List<Member> members) throws Exception {

		Dimension dimension = getDimension(memberWidth, memberHeight, members);

		BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHints(rh);

		Font font = new Font("Arial", Font.PLAIN, 10);
		graphics.setFont(font);

		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, dimension.width + memberWidth, dimension.height + memberHeight);

		drawNames(1, 1, members, graphics);

		return image;
	}

	private Dimension getDimension(int width, int height, List<Member> members) {

		width = width - memberWidth;

		for (Member member : members) {
			width = width + memberWidth;
			if (member instanceof Couple) {
				Couple couple = (Couple) member;
				if (!couple.getDescendants().isEmpty()) {
					Dimension dimension = getDimension(width, height + memberHeight, couple.getDescendants());
					width = Math.max(width, dimension.width);
					height = Math.max(height, dimension.height);
				}
			}
		}

		return new Dimension(width, height);
	}

	private Dimension drawNames(int horizontalPosition, int verticalPosiotion, List<Member> members,
			Graphics2D graphics) {

		horizontalPosition--;

		for (Member member : members) {
			horizontalPosition++;

			if (member instanceof Person) {
				Person person = (Person) member;
				graphics.setColor(Color.black);

				int nameWidth = graphics.getFontMetrics().stringWidth(person.getName());

				graphics.drawImage(imageProvider.getImage(person),
						horizontalPosition * memberWidth - nameWidth - ((memberWidth - nameWidth) / 2),
						verticalPosiotion * memberHeight - memberHeight / 2, null);

				graphics.drawString(person.getName(),
						horizontalPosition * memberWidth - nameWidth - ((memberWidth - nameWidth) / 2),
						verticalPosiotion * memberHeight - memberHeight / 2);

			}

			else if (member instanceof Couple) {
				Couple couple = (Couple) member;
				graphics.setColor(Color.black);

				int nameWidth = graphics.getFontMetrics()
						.stringWidth(couple.getPerson().getName() + " + " + couple.getPartner().getName());

				graphics.drawImage(imageProvider.getImage(couple.getPerson()),
						horizontalPosition * memberWidth - nameWidth - ((memberWidth - nameWidth) / 2),
						verticalPosiotion * memberHeight - memberHeight / 2, null);

				graphics.drawString(couple.getPerson().getName() + " + " + couple.getPartner().getName(),
						horizontalPosition * memberWidth - nameWidth - ((memberWidth - nameWidth) / 2),
						verticalPosiotion * memberHeight - memberHeight / 2);

				if (!couple.getDescendants().isEmpty()) {
					Dimension dimension = drawNames(horizontalPosition, verticalPosiotion + 1, couple.getDescendants(),
							graphics);
					horizontalPosition = Math.max(horizontalPosition, dimension.width);
				}
			}

		}

		return new Dimension(horizontalPosition, verticalPosiotion);
	}
}

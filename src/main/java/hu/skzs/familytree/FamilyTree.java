package hu.skzs.familytree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.skzs.familytree.memberreader.MemberReader;
import hu.skzs.familytree.renderer.Renderer;

@SpringBootApplication
public class FamilyTree implements CommandLineRunner
{
	private static Logger LOG = LoggerFactory.getLogger(FamilyTree.class);

	@Autowired
	private MemberReader memberReader;

	@Autowired
	private Renderer renderer;

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(FamilyTree.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : command line runner");

		if (args.length != 2) {
			throw new IllegalArgumentException("Inbvalid parameters");
		}

		File csvFile = new File(args[0]);

		List<Member> members = memberReader.read(csvFile);
		BufferedImage image = renderer.renderer(members);
		ImageIO.write(image, "PNG", new File(args[1]));

	}
}

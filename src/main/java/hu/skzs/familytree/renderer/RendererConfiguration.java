package hu.skzs.familytree.renderer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RendererConfiguration {

	@Bean
	public Renderer renderer() {
		return new Renderer(imageProvider());
	}

	@Bean
	public ImageProvider imageProvider() {
		return new ImageProvider();
	}

}

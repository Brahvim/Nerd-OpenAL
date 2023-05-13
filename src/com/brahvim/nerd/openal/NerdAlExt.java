package com.brahvim.nerd.openal;

import java.util.function.Consumer;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.processing_wrapper.NerdCustomSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdExt;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PConstants;
import processing.core.PVector;

public class NerdAlExt extends NerdExt {

	private AlContext.AlContextSettings settings;

	// region Constructors.
	public NerdAlExt(final AlContext.AlContextSettings p_settings) {
		this.settings = p_settings;
	}

	public NerdAlExt(final Consumer<AlContext.AlContextSettings> p_settingsBuilder) {
		final var toPass = new AlContext.AlContextSettings();
		if (p_settingsBuilder != null)
			p_settingsBuilder.accept(toPass);
		this.settings = toPass;
	}
	// endregion

	@Override
	public String getExtName() {
		return "OpenAL";
	}

	@Override
	public Object init(final NerdCustomSketchBuilder p_builder) {
		final NerdAl toRet = new NerdAl(this.settings);

		// When the scene is changed, delete unnecessary OpenAL data:
		p_builder.addSketchConstructionListener(s -> s.getSceneManager()
				.addSceneChangeListener((a, p, c) -> {
					if (p != null)
						toRet.scenelyDisposal();
					toRet.unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;
				}));

		// We have framely callbacks, yes:
		p_builder.addDrawListener(s -> this.provideDrawListener(s, toRet));

		// When the NerdSketch is exiting, delete all OpenAL native data:
		p_builder.addSketchDisposalListener(s -> toRet.completeDisposal());

		return toRet;
	}

	protected Consumer<NerdSketch> provideDrawListener(final NerdSketch p_NerdSketch, final NerdAl p_alInst) {
		switch (p_NerdSketch.RENDERER) {
			case PConstants.P2D, PConstants.P3D -> {
				// I wanted to declare this lambda as an anon class instead, but I wanted to
				// watch this trick where I have a variable from outside the lambda work there.
				// ...It does!:
				final PVector lastCameraPos = new PVector();

				return s -> {
					// Process everything, every frame!:
					p_alInst.framelyCallback();

					final NerdAbstractCamera camera = p_NerdSketch.getCamera();
					final PVector camPos = camera.getPos(), camUp = camera.getUp();

					p_alInst.setListenerOrientation(camUp.x, camUp.y, camUp.z);

					p_alInst.setListenerVelocity(
							camPos.x - lastCameraPos.x,
							camPos.y - lastCameraPos.y,
							camPos.z - lastCameraPos.z);
					// PVector.div((PVector.sub(camPos, lastCameraPos)).array(),
					// this.unitSize));

					// JIT-style optimization + protection from `0`!:
					if (p_alInst.unitSize == 1.0f || p_alInst.unitSize == 0.0f) {
						p_alInst.setListenerPosition(camPos.x, camPos.y, camPos.z);
					} else {
						// final PVector listenerPos = PVector.div(camPos, toRet.unitSize);
						// toRet.setListenerPosition(listenerPos.x, listenerPos.y, listenerPos.z);
						p_alInst.setListenerPosition(
								camPos.x / p_alInst.unitSize,
								camPos.y / p_alInst.unitSize,
								camPos.z / p_alInst.unitSize);
					}

					lastCameraPos.set(camPos);
				};
			}
		}

		// Renderer does not have a camera? No worries! Users handle this stuff now!
		// We just... Process everything, every frame! (As we always do! ^-^):
		return s -> p_alInst.framelyCallback();
	}

}

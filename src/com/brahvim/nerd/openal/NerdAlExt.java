package com.brahvim.nerd.openal;

import java.util.function.Consumer;

import com.brahvim.nerd.papplet_wrapper.CustomSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.NerdExt;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.rendering.cameras.NerdAbstractCamera;

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
	public Object init(final CustomSketchBuilder p_builder) {
		final NerdAl toRet = new NerdAl(this.settings);

		// When the scene is changed, delete unnecessary OpenAL data:
		p_builder.addSketchConstructionListener(s -> s.getSceneManager()
				.addSceneChangedListener((a, p, c) -> {
					if (p != null)
						toRet.scenelyDisposal();
					toRet.unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;
				}));

		// We have framely callbacks, yes:
		p_builder.addDrawListener(s -> this.provideDrawListener(s, toRet));

		// When the sketch is exiting, delete all OpenAL native data:
		p_builder.addSketchDisposalListener(s -> toRet.completeDisposal());

		return toRet;
	}

	protected Consumer<Sketch> provideDrawListener(final Sketch p_sketch, final NerdAl p_alInst) {
		switch (p_sketch.RENDERER) {
			case PConstants.P2D, PConstants.P3D -> {
				// I wanted to declare this lambda as an anon class instead, but I wanted to
				// watch this trick where I have a variable from outside the lambda work there.
				// ...It does!:
				final PVector lastCameraPos = new PVector();

				return s -> {
					// Process everything, every frame!:
					p_alInst.framelyCallback();

					final NerdAbstractCamera camera = p_sketch.getCamera();
					p_alInst.setListenerOrientation(camera.up.x, camera.up.y, camera.up.z);

					p_alInst.setListenerVelocity(
							camera.pos.x - lastCameraPos.x,
							camera.pos.y - lastCameraPos.y,
							camera.pos.z - lastCameraPos.z);
					// PVector.div((PVector.sub(camera.pos, lastCameraPos)).array(),
					// this.unitSize));

					// JIT-style optimization + protection from `0`!:
					if (p_alInst.unitSize == 1.0f || p_alInst.unitSize == 0.0f) {
						p_alInst.setListenerPosition(camera.pos.x, camera.pos.y, camera.pos.z);
					} else {
						// final PVector listenerPos = PVector.div(camera.pos, toRet.unitSize);
						// toRet.setListenerPosition(listenerPos.x, listenerPos.y, listenerPos.z);
						p_alInst.setListenerPosition(
								camera.pos.x / p_alInst.unitSize,
								camera.pos.y / p_alInst.unitSize,
								camera.pos.z / p_alInst.unitSize);
					}

					lastCameraPos.set(camera.pos);
				};
			}
		}

		// Renderer does not have a camera? No worries! Users handle this stuff now!
		// We just... Process everything, every frame! (As we always do! ^-^):
		return s -> p_alInst.framelyCallback();
	}

}

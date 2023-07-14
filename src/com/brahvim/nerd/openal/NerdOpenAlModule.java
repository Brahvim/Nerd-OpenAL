package com.brahvim.nerd.openal;

import java.util.function.Consumer;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilderSettings;

import processing.core.PVector;

public class NerdOpenAlModule extends NerdModule {

	private final AlContext.AlContextSettings SETTINGS;
	private final PVector lastCameraPos = new PVector();

	private NerdAl alMan;

	// region Constructors.
	public NerdOpenAlModule(final NerdSketch p_sketch, final AlContext.AlContextSettings p_settings) {
		super(p_sketch);
		this.SETTINGS = p_settings;
	}

	public NerdOpenAlModule(final NerdSketch p_sketch, final Consumer<AlContext.AlContextSettings> p_settingsBuilder) {
		super(p_sketch);

		final var toPass = new AlContext.AlContextSettings();
		if (p_settingsBuilder != null)
			p_settingsBuilder.accept(toPass);
		this.SETTINGS = toPass;
	}
	// endregion

	public NerdAl getOpenAlManager() {
		return this.alMan;
	}

	@Override
	public void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
		this.alMan = new NerdAl(this.SETTINGS);

		// When the scene is changed, delete unnecessary OpenAL data:
		super.SKETCH.getNerdModule(NerdScenesModule.class)
				.addNewSceneStartedListener((a, p, c) -> { // A mispronounced alphabets joke!
					if (p != null)
						this.alMan.scenelyDisposal();
				});

		// We have framely callbacks, yes:
		// p_builder.addDrawListener(s -> this.provideDrawListener(s, this.alInst));

		// return this.alInst;
	}

	@Override
	protected void draw() {
		// Renderer does not have a camera?
		// No worries!
		// Users handle setting listener properties now!
		// We just... Process everything, every frame! (As we always do! ^-^):
		if (!super.SKETCH.SKETCH_SETTINGS.USES_OPENGL) {
			this.alMan.framelyCallback();
			return;
		}

		// I wanted to declare this lambda as an anon class instead, but I wanted to
		// watch this trick where I have a variable from outside the lambda work there.
		// ...It does!:

		// Process everything, every frame!:
		this.alMan.framelyCallback();

		final NerdAbstractCamera camera = super.SKETCH.getNerdGraphics().getCurrentCamera();
		final PVector camPos = camera.getPos(), camUp = camera.getUp();

		this.alMan.setListenerOrientation(camUp.x, camUp.y, camUp.z);

		this.alMan.setListenerVelocity(
				camPos.x - lastCameraPos.x,
				camPos.y - lastCameraPos.y,
				camPos.z - lastCameraPos.z);

		this.alMan.setListenerPosition(camPos.x, camPos.y, camPos.z);

		this.lastCameraPos.set(camPos);
	}

	@Override
	// When the NerdSketch is exiting, delete all OpenAL native data:
	protected void dispose() {
		this.alMan.completeDisposal();
	}

}

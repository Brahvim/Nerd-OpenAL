package com.brahvim.nerd.openal;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdModuleSettings;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilderSettings;

import processing.core.PVector;

public class NerdOpenAlModule extends NerdModule {

	// region Fields.
	/**
	 * When set to {@code true}, the properties from the {@link NerdAbstractCamera},
	 * set using {@link NerdOpenAlModule#setCameraToTrack(NerdAbstractCamera)}
	 * are used to set OpenAL listener parameters.
	 */
	public boolean letCameraSetListener = true;

	/**
	 * Is the camera you want to us track, always contained in the default
	 * {@link NerdGraphics} buffer of the sketch? If so, set this field to
	 * {@code true}, to automatically track the camera present there.
	 */
	public boolean trackedCamIsDefaultCam = true;

	private final PVector lastCameraPos = new PVector();

	private final AlContext.AlContextSettings alModuleSettings = new AlContext.AlContextSettings();
	private NerdAbstractCamera trackedCamera;
	private NerdAl alMan;
	// endregion

	// region Construction.
	public NerdOpenAlModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	@Override
	protected void assignModuleSettings(final NerdModuleSettings<?> p_settings) {
		// This is, *secretly*, also a check for `null`!:
		if (p_settings instanceof final NerdOpenAlModuleSettings moduleSettings) {
			this.alModuleSettings.frequency = moduleSettings.frequency;
			this.alModuleSettings.monoSources = moduleSettings.monoSources;
			this.alModuleSettings.stereoSources = moduleSettings.stereoSources;
			this.alModuleSettings.refresh = moduleSettings.refresh;
			this.alModuleSettings.sync = moduleSettings.sync;
		}
	}
	// endregion

	public NerdAl getOpenAlManager() {
		return this.alMan;
	}

	public NerdAbstractCamera getTrackedCamera() {
		return this.trackedCamera;
	}

	public void setCameraToTrack(final NerdAbstractCamera p_cameraToTrack) {
		this.trackedCamera = p_cameraToTrack;
	}

	// region `NerdSketch` events!
	@Override
	public void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
		this.alMan = new NerdAl(this.alModuleSettings);

		// When the scene is changed, delete unnecessary OpenAL data:
		super.SKETCH.getNerdModule(NerdScenesModule.class)
				.addNewSceneStartedListener((a, p, c) -> { // A mispronounced alphabets joke!
					if (p != null)
						this.alMan.scenelyDisposal();
				});
	}

	@Override
	protected void postSetup() {
		this.trackedCamera = super.SKETCH.getNerdGraphics().getCurrentCamera();
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

		// ...If the user does not want the automation,
		// process everything, every frame!
		// ...and return!
		if (!this.letCameraSetListener) {
			this.alMan.framelyCallback();
			return;
		}

		final NerdAbstractCamera camera = super.SKETCH.getNerdGraphics().getCurrentCamera();
		final PVector camPos = camera.getPos(), camUp = camera.getUp();

		this.alMan.setListenerOrientation(camUp.x, camUp.y, camUp.z);

		this.alMan.setListenerVelocity(
				camPos.x - lastCameraPos.x,
				camPos.y - lastCameraPos.y,
				camPos.z - lastCameraPos.z);

		this.alMan.setListenerPosition(camPos.x, camPos.y, camPos.z);

		this.lastCameraPos.set(camPos);

		// Process everything, every frame!:
		this.alMan.framelyCallback();
	}

	@Override
	protected void dispose() {
		// When the NerdSketch is exiting, delete all OpenAL native data:
		this.alMan.completeDisposal();
	}
	// endregion

}

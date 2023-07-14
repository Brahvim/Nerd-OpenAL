package com.brahvim.nerd.openal;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PVector;

public class NerdAlUpdater {

	// region Fields.
	/**
	 * When set to {@code true}, the properties from the {@link NerdAbstractCamera},
	 * set using {@link NerdAlUpdater#setCameraToTrack(NerdAbstractCamera)}
	 * are used to set OpenAL listener parameters.
	 *
	 * @apiNote Set to {@code true} by default!
	 */
	public boolean trackCameraAutomatically = true;

	/**
	 * Is the camera you want to us track, always contained in the default
	 * {@link NerdGraphics} buffer of the sketch? If so, set this field to
	 * {@code true}, to automatically track the camera present there.
	 *
	 * @apiNote
	 *          Set to {@code true} by default!
	 */
	public boolean trackedCamIsDefaultCam = true;

	/**
	 * Individual option for {@link NerdAbstractCamera} tracking. Effective only if
	 * {@link NerdAlUpdater#trackCameraAutomatically} is {@code true}.
	 */
	public boolean trackCameraPos, trackCameraOrientation, trackCameraVel;

	protected final PVector LAST_CAM_POS = new PVector();
	protected final NerdSketch SKETCH;
	protected final NerdAl MAN;

	protected NerdAbstractCamera trackedCamera;
	protected float velocityCoef = 0.25f;
	// endregion

	public NerdAlUpdater(final NerdSketch p_sketch, final NerdAl p_alMan) {
		this.MAN = p_alMan;
		this.SKETCH = p_sketch;
	}

	protected void framelyCallback() {
		// Renderer does not have a camera?
		// No worries!
		// Users handle setting listener properties now!
		// We just... Process everything, every frame! (As we always do! ^-^):
		if (!this.SKETCH.SKETCH_SETTINGS.USES_OPENGL) {
			this.MAN.framelyCallback();
			return;
		}

		// ...If the user does not want the automation,
		// process everything, every frame!
		// ...and return!
		if (!this.trackCameraAutomatically) {
			this.MAN.framelyCallback();
			return;
		}

		// Process everything, every frame!:
		this.MAN.framelyCallback();

		if (this.trackedCamIsDefaultCam)
			this.trackedCamera = this.SKETCH.getNerdGraphics().getCurrentCamera();

		this.trackCamera();

		// Not passed by reference. What if `camPos` is `null`?!
	}

	protected void trackCamera() {
		final PVector camPos = this.trackedCamera.getPos(), camUp = this.trackedCamera.getUp();

		if (this.trackCameraOrientation)
			this.MAN.setListenerOrientation(camUp.x, camUp.y, camUp.z);

		if (this.trackCameraVel)
			this.MAN.setListenerVelocity(
					this.velocityCoef * (camPos.x - LAST_CAM_POS.x),
					this.velocityCoef * (camPos.y - LAST_CAM_POS.y),
					this.velocityCoef * (camPos.z - LAST_CAM_POS.z));

		if (this.trackCameraPos)
			this.MAN.setListenerPosition(camPos.x, camPos.y, camPos.z);

		this.LAST_CAM_POS.set(camPos);
	}

	// region Getters and setters.
	public NerdAl getUnderlyingManager() {
		return this.MAN;
	}

	public float getVelocityCoef() {
		return this.velocityCoef;
	}

	public void setVelocityCoef(final float p_coefficient) {
		this.velocityCoef = p_coefficient;
	}

	public NerdAbstractCamera getTrackedCamera() {
		return this.trackedCamera;
	}

	public void setCameraToTrack(final NerdAbstractCamera p_cameraToTrack) {
		this.trackedCamera = p_cameraToTrack;
	}
	// endregion

}

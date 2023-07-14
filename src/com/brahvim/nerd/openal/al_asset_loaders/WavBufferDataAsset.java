package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdOpenAlModule;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

/**
 * @deprecated since the Java APIs don't function in our favor here - to make
 *             this work, I'll need a way to convert!
 */
@Deprecated
public class WavBufferDataAsset extends NerdSinglePathAssetLoader<AlWavBuffer> {

	public WavBufferDataAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected AlWavBuffer fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final NerdAl alMan = p_sketch.getNerdModule(NerdOpenAlModule.class).getOpenAlManager();
			final AlWavBuffer wavBuffer = new AlWavBuffer(alMan);
			wavBuffer.shouldDispose(false);
			wavBuffer.loadFrom(super.path);
			return wavBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException(this);
			// return ByteBuffer.allocate(0);
		}
	}

}

package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

@Deprecated
public class WavBufferDataAsset extends NerdSinglePathAssetLoader<AlWavBuffer> {

	public WavBufferDataAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected AlWavBuffer fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlWavBuffer wavBuffer = new AlWavBuffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			wavBuffer.shouldDispose(false);
			wavBuffer.loadFrom(super.path);
			return wavBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ByteBuffer.allocate(0);
		}
	}

}

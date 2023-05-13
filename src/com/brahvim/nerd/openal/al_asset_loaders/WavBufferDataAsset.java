package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

@Deprecated
public class WavBufferDataAsset extends NerdAssetLoader<AlWavBuffer> {

	@Override
	public AlWavBuffer fetchData(final NerdSketch p_NerdSketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlWavBuffer wavBuffer = new AlWavBuffer((NerdAl) p_NerdSketch.getNerdExt("OpenAL"));
			wavBuffer.shouldDispose(false);
			wavBuffer.loadFrom(p_path);
			return wavBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ByteBuffer.allocate(0);
		}
	}

}

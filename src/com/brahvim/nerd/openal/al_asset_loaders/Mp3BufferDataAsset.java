package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlMp3Buffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

@Deprecated
public class Mp3BufferDataAsset extends NerdAssetLoader<AlMp3Buffer> {

	@Override
	public AlMp3Buffer fetchData(final NerdSketch p_NerdSketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlMp3Buffer mp3Buffer = new AlMp3Buffer((NerdAl) p_NerdSketch.getNerdExt("OpenAL"));
			mp3Buffer.shouldDispose(false);
			mp3Buffer.loadFrom(p_path);
			return mp3Buffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ShortBuffer.allocate(0);
		}
	}

}

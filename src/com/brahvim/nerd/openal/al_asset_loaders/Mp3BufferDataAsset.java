package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlMp3Buffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

@Deprecated
public class Mp3BufferDataAsset extends NerdSinglePathAssetLoader<AlMp3Buffer> {

	public Mp3BufferDataAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected AlMp3Buffer fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlMp3Buffer mp3Buffer = new AlMp3Buffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			mp3Buffer.shouldDispose(false);
			mp3Buffer.loadFrom(super.path);
			return mp3Buffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ShortBuffer.allocate(0);
		}
	}

}

package binnie.extratrees.proxy;

import javax.annotation.Nonnull;

import binnie.Constants;
import binnie.core.models.ModelManager;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.block.BlockETDecorativeLeaves;
import binnie.extratrees.block.ModuleBlocks;
import binnie.extratrees.block.wood.BlockETSlab;
import binnie.extratrees.models.ModelETDecorativeLeaves;
import forestry.arboriculture.PluginArboriculture;
import forestry.core.models.BlockModelEntry;
import forestry.core.models.ModelEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends Proxy implements IExtraTreeProxy {
	public static ModelManager modelManager = new ModelManager(Constants.EXTRA_TREES_MOD_ID);

	@Override
	public void init() {
		//ForestryAPI.textureManager.registerIconProvider(FruitSprite.Average);
	}
	
	@Override
	public void registerBlockModel(@Nonnull final BlockModelEntry index) {
		ModelManager.registerCustomBlockModel(index);
		if(index.addStateMapper){
			StateMapperBase ignoreState = new BlockModeStateMapper(index);
			ModelLoader.setCustomStateMapper(index.block, ignoreState);
		}
	}
	
	@Override
	public void registerModel(@Nonnull ModelEntry index) {
		ModelManager.registerCustomModel(index);
	}

	@Override
	public void setCustomStateMapper(String name, Block block) {
		ModelLoader.setCustomStateMapper(block, new CustomMapper(name));
	}

	public static ModelManager getModelManager() {
		return modelManager;
	}

	@Override
	public Item registerItem(Item item) {
		getModelManager().registerItemClient(item);
		return super.registerItem(item);
	}

	@Override
	public Block registerBlock(Block block) {
		getModelManager().registerBlockClient(block);
		return super.registerBlock(block);
	}

	@Override
	public void registerModels() {
		ExtraTrees.blocks();
		for (BlockETDecorativeLeaves leaves : ModuleBlocks.leavesDecorative) {
			String resourceName = leaves.getRegistryName().toString();
			ModelResourceLocation blockModelLocation = new ModelResourceLocation(resourceName);
			ModelResourceLocation itemModeLocation = new ModelResourceLocation(resourceName, "inventory");
			BlockModelEntry blockModelIndex = new BlockModelEntry(blockModelLocation, itemModeLocation, new ModelETDecorativeLeaves(), leaves);
			registerBlockModel(blockModelIndex);
		}
		
		ExtraTrees.blocks();
		for(BlockETSlab slab : ModuleBlocks.slabsDouble){
			PluginArboriculture.proxy.registerWoodModel(slab, true);
		}
		ExtraTrees.blocks();
		for(BlockETSlab slab : ModuleBlocks.slabsDoubleFireproof){
			PluginArboriculture.proxy.registerWoodModel(slab, true);
		}
		getModelManager().registerModels();
	}

	@Override
	public void registerItemAndBlockColors() {
		getModelManager().registerItemAndBlockColors();
	}
	
	class CustomMapper extends StateMapperBase {
		ResourceLocation rl;

		public CustomMapper(String name) {
			rl = new ResourceLocation(Constants.EXTRA_TREES_MOD_ID, name);
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return new ModelResourceLocation(rl, this.getPropertyString(state.getProperties()));
		}
	}
	
	class BlockModeStateMapper extends StateMapperBase {
		private final BlockModelEntry index;

		public BlockModeStateMapper(BlockModelEntry index) {
			this.index = index;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
			return index.blockModelLocation;
		}
	}

}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package biomesoplenty.common.handler;

public class AchievementEventHandler
{
    /*private static final Set<Biome> BOP_BIOMES_TO_EXPLORE = Sets.union(BOPBiomes.REG_INSTANCE.getPresentBiomes(), Biome.EXPLORATION_BIOMES_LIST);

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.getItem().getEntityItem();
        Item item = stack.getItem();
        
        IBlockState state = Block.getBlockFromItem(item).getStateFromMeta(item.getMetadata(stack));
        Block block = state.getBlock();
        EntityPlayer player = event.getEntityPlayer();

        if (block instanceof BlockBOPLog)
        {
            player.addStat(AchievementList.MINE_WOOD);
        }

        //Flower Child Achievement
        if (block instanceof BlockBOPFlower || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER)
        {
            player.addStat(BOPAchievements.obtain_flowers);
        }

        //Berry Good Achievement
        if (item == BOPItems.berries)
        {
            player.addStat(BOPAchievements.obtain_berry);
        }
        
        //Totally Coral Achievement
        if (block == BOPBlocks.coral)
        {
            player.addStat(BOPAchievements.obtain_coral);
        }
        
        //Life Finds a Way Achievement
        if (state == BlockBOPFlower.paging.getVariantState(BOPFlowers.MINERS_DELIGHT))
        {
            player.addStat(BOPAchievements.obtain_miners_delight);
        }


        //Rather Thorny Achievement
        if (state == BlockBOPPlant.paging.getVariantState(BOPPlants.THORN))
        {
            player.addStat(BOPAchievements.obtain_thorn);
        }
        
        //I am Become Death Achievement
        if (state == BlockBOPFlower.paging.getVariantState(BOPFlowers.DEATHBLOOM))
        {
            player.addStat(BOPAchievements.obtain_deathbloom);
        }
        
        //Godsend Achievement
        if (state == BlockBOPFlower.paging.getVariantState(BOPFlowers.WILTED_LILY))
        {
            player.addStat(BOPAchievements.obtain_wilted_lily);
        }

        //Stalk Market Achievement
        if (item == BOPItems.turnip)
        {
            player.addStat(BOPAchievements.obtain_turnip);
        }
        
        //Soul Searching Achievement
        if (item == BOPItems.soul)
        {
            player.addStat(BOPAchievements.obtain_soul);
        }

        //Honeycomb's Big Achievement
        if (item == BOPItems.filled_honeycomb)
        {
            player.addStat(BOPAchievements.obtain_honeycomb);
        }

        //Don't Breathe This Achievement
        if (item == BOPItems.pixie_dust)
        {
            player.addStat(BOPAchievements.obtain_pixie_dust);
        }

        //Far Out Achievement
        if (item == BOPItems.crystal_shard)
        {
            player.addStat(BOPAchievements.obtain_celestial_crystal);
        }
    }
    
    @SubscribeEvent
    public void onItemUsed(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
    }
    
    @SubscribeEvent
    public void onItemUsed(LivingEntityUseItemEvent.Finish event)
    {
        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();

            //Trippin'
            if (item == BOPItems.shroompowder) {
                player.addStat(BOPAchievements.eat_shroom_powder);
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event)
    {
        ItemStack stack = event.getItemInHand();
        
        //Blocks can be placed by things other than players
        if (stack != null)
        {
            Item item = stack.getItem();
            IBlockState state = Block.getBlockFromItem(item).getStateFromMeta(item.getMetadata(stack));

            try
            {
                //Yggdrasil
                if (state == BlockBOPSapling.paging.getVariantState(BOPTrees.SACRED_OAK))
                {
                    event.getPlayer().addStat(BOPAchievements.grow_sacred_oak);
                }
            }
            catch(Exception ignored) {} //Fail quietly if there's a problem matching metadata to a block state
        }
    }
    
    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();
        EntityPlayer player = event.player;
        
        //Nectar of the Gods Achievement
        if (item == BOPItems.ambrosia)
        {
            player.addStat(BOPAchievements.craft_ambrosia);
        }
        
        //By Your Powers Combined Achievement
        if (item == BOPItems.terrestrial_artifact)
        {
            player.addStat(BOPAchievements.craft_terrestrial_artifact);
        }
        
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event)
    {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            //Check every five seconds if the player has entered a new biome, if they haven't already gotten the achievement
            if (player.ticksExisted % 20 * 5 == 0)
            {
                //Search Party
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.use_biome_finder))
                {
                    this.updateBiomeRadarExplore(player);
                }

                //The Wanderer
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.explore_all_biomes)) // TODO Test
                {
                    this.updateBiomesExplored(player);
                }
            }
        }
    }

    private void updateBiomeRadarExplore(EntityPlayerMP player)
    {
        Biome currentBiome = player.world.getBiome(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ)));

        //Search every item in the player's main inventory for a biome radar
        for (ItemStack stack : player.inventory.mainInventory)
        {
            //If the stack is null, skip it
            if (stack.isEmpty())
                continue;
            
            if (stack.getItem() == BOPItems.biome_finder && stack.hasTagCompound() && stack.getTagCompound().hasKey("biomeIDToFind"))
            {
                int biomeIdToFind = stack.getTagCompound().getInteger("biomeIDToFind");

                //If the current biome id is the id on the radar, award the achievement and stop searching
                if (biomeIdToFind == Biome.getIdForBiome(currentBiome)) 
                {
                    player.addStat(BOPAchievements.use_biome_finder);
                    return;
                }
            }
        }
    }
    
    private void updateBiomesExplored(EntityPlayerMP player)
    {
        Biome currentBiome = player.world.getBiome(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ)));
        String biomeName = currentBiome.getBiomeName();
        //Get a list of the current explored biomes
        JsonSerializableSet exploredBiomeNames = player.getStatFile().getProgress(BOPAchievements.explore_all_biomes);

        if (exploredBiomeNames == null)
        {
            //Set the stat data
            exploredBiomeNames = player.getStatFile().setProgress(BOPAchievements.explore_all_biomes, new JsonSerializableSet());
        }

        //Add the current biome to the set of biomes that the player has explored
        exploredBiomeNames.add(biomeName);

        if (player.getStatFile().canUnlockAchievement(BOPAchievements.explore_all_biomes) && exploredBiomeNames.size() >= BOP_BIOMES_TO_EXPLORE.size())
        {
            //Create a copy of the set of biomes that need to be explored to fulfil the achievement
            Set<Biome> set = Sets.newHashSet(BOP_BIOMES_TO_EXPLORE);

            //Iterate over the names of all the biomes the player has explored
            for (String exploredBiomeName : exploredBiomeNames)
            {
                Iterator<Biome> iterator = set.iterator();

                //Iterate over the set of biomes required to be explored and remove those that already have been explored
                while (iterator.hasNext())
                {
                    Biome biome = iterator.next();

                    if (biome.getBiomeName().equals(exploredBiomeName))
                    {
                        iterator.remove();
                    }
                }

                //If there are no biomes remaining in the set to be explored, then there's no point continuing
                if (set.isEmpty())
                {
                    break;
                }
            }

            //Has the player fulfilled the achievement (there are no biomes left in the set of biomes to be explored)
            if (set.isEmpty())
            {
                player.addStat(BOPAchievements.explore_all_biomes);
            }
        }
    }*/
}

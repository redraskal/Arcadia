package me.jedimastersoda.arcadia.paper.servertype.game.gamemode.rocketroyal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;

public class RocketRoyalEndCrystal {

  private final Location spawnLocation;
  private EnderCrystal enderCrystal;
  private final Block glassBlock;
  private final List<Block> woolBlocks = new ArrayList<>();
  private final List<Block> beaconBlocks = new ArrayList<>();

  public RocketRoyalEndCrystal(Location spawnLocation) {
    this.spawnLocation = spawnLocation;
    this.glassBlock = spawnLocation.getBlock().getRelative(BlockFace.DOWN);

    woolBlocks.add(glassBlock.getRelative(BlockFace.NORTH));
    woolBlocks.add(glassBlock.getRelative(BlockFace.NORTH_EAST));
    woolBlocks.add(glassBlock.getRelative(BlockFace.EAST));
    woolBlocks.add(glassBlock.getRelative(BlockFace.SOUTH_EAST));
    woolBlocks.add(glassBlock.getRelative(BlockFace.SOUTH));
    woolBlocks.add(glassBlock.getRelative(BlockFace.SOUTH_WEST));
    woolBlocks.add(glassBlock.getRelative(BlockFace.WEST));
    woolBlocks.add(glassBlock.getRelative(BlockFace.NORTH_WEST));

    for(Block woolBlock : woolBlocks) {
      beaconBlocks.add(woolBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN));
    }

    glassBlock.getRelative(BlockFace.DOWN).setType(Material.BEACON);

    beaconBlocks.add(glassBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN));

    this.resetPowerup();
  }

  public void spawnPowerup() {
    this.enderCrystal = spawnLocation.getWorld().spawn(spawnLocation, EnderCrystal.class);
    enderCrystal.setCustomName(ChatColor.translateAlternateColorCodes('&', "&a&lPOWER UP READY"));
    enderCrystal.setCustomNameVisible(true);

    glassBlock.setType(Material.LIME_STAINED_GLASS);

    for(Block woolBlock : woolBlocks) {
      woolBlock.setType(Material.LIME_WOOL);
    }

    for(Block beaconBlock : beaconBlocks) {
      beaconBlock.setType(Material.EMERALD_BLOCK);
    }
  }

  public void resetPowerup() {
    if(this.enderCrystal != null && !this.enderCrystal.isDead()) {
      this.enderCrystal.remove();
    }

    glassBlock.setType(Material.WHITE_STAINED_GLASS);

    for(Block woolBlock : woolBlocks) {
      woolBlock.setType(Material.WHITE_WOOL);
    }

    for(Block beaconBlock : beaconBlocks) {
      beaconBlock.setType(Material.BEDROCK);
    }
  }
}
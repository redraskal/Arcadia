package me.jedimastersoda.arcadia.common.highway;

import me.jedimastersoda.arcadia.common.object.ServerStatus;

public abstract class HighwayListener {

  public abstract void onServerUpdate(ServerStatus serverStatus);
}
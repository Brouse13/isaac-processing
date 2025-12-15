package es.uib.isaac.entity;

import es.uib.isaac.assets.Asset;

import java.util.UUID;

public record Item(UUID uuid, String name, Asset asset) { }

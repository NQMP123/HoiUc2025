using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Optimized game loop management to fix FPS fluctuation issues
/// Replaces heavy update loops with more efficient alternatives
/// </summary>
public static class OptimizedGameLoop
{
    // Update throttling counters
    private static int effectUpdateFrame = 0;
    private static int entityUpdateFrame = 0;
    private static int backgroundUpdateFrame = 0;
    
    // Cached collection sizes to avoid repeated .size() calls
    private static Dictionary<string, int> cachedSizes = new Dictionary<string, int>();
    private static int sizeCacheFrame = 0;
    
    // Entity update batching
    private static int maxEntitiesPerFrame = 10;
    private static int currentEntityBatch = 0;
    
    /// <summary>
    /// Optimized character updates with batching
    /// </summary>
    public static void UpdateCharactersOptimized(MyVector vCharInMap)
    {
        if (vCharInMap == null) return;
        
        int totalChars = GetCachedSize("characters", vCharInMap.size());
        if (totalChars == 0) return;
        
        // Update only a batch of characters per frame
        int startIndex = currentEntityBatch * maxEntitiesPerFrame;
        int endIndex = Math.Min(startIndex + maxEntitiesPerFrame, totalChars);
        
        for (int i = startIndex; i < endIndex; i++)
        {
            if (i < totalChars)
            {
                Char character = (Char)vCharInMap.elementAt(i);
                if (character != null && IsEntityRelevant(character))
                {
                    character.update();
                }
            }
        }
        
        // Move to next batch
        currentEntityBatch++;
        if (startIndex >= totalChars)
        {
            currentEntityBatch = 0;
        }
    }
    
    /// <summary>
    /// Optimized mob updates with relevance checking
    /// </summary>
    public static void UpdateMobsOptimized(MyVector vMob)
    {
        if (vMob == null || PerformanceOptimizer.ShouldSkipUpdate("mob_update", 2)) 
            return;
        
        int totalMobs = GetCachedSize("mobs", vMob.size());
        
        for (int i = 0; i < totalMobs; i++)
        {
            Mob mob = (Mob)vMob.elementAt(i);
            if (mob != null && IsEntityRelevant(mob))
            {
                mob.update();
            }
        }
    }
    
    /// <summary>
    /// Optimized effect updates with batching and culling
    /// </summary>
    public static void UpdateEffectsOptimized()
    {
        effectUpdateFrame++;
        
        // Update different effect types on different frames
        switch (effectUpdateFrame % 4)
        {
            case 0:
                UpdateEffectType("main_effects");
                break;
            case 1:
                UpdateEffectType("char_effects");
                break;
            case 2:
                UpdateEffectType("background_effects");
                break;
            case 3:
                UpdateEffectType("ui_effects");
                break;
        }
    }
    
    /// <summary>
    /// Update specific effect type
    /// </summary>
    private static void UpdateEffectType(string effectType)
    {
        // This would be implemented based on the specific effect collections
        // For now, this is a placeholder for the pattern
        switch (effectType)
        {
            case "main_effects":
                if (EffecMn.vEffecMn != null)
                {
                    UpdateEffectCollection(EffecMn.vEffecMn);
                }
                break;
            // Add other effect types as needed
        }
    }
    
    /// <summary>
    /// Optimized effect collection update
    /// </summary>
    private static void UpdateEffectCollection(MyVector effectCollection)
    {
        if (effectCollection == null) return;
        
        int totalEffects = GetCachedSize("effects", effectCollection.size());
        
        // Process effects in smaller batches
        int batchSize = Math.Min(5, totalEffects);
        int startIndex = (effectUpdateFrame * batchSize) % totalEffects;
        
        for (int i = 0; i < batchSize && (startIndex + i) < totalEffects; i++)
        {
            object effect = effectCollection.elementAt(startIndex + i);
            if (effect != null)
            {
                // Update effect (implementation depends on effect type)
                if (effect is Effect)
                {
                    ((Effect)effect).update();
                }
            }
        }
    }
    
    /// <summary>
    /// Check if an entity is relevant for updates (on screen, within range, etc.)
    /// </summary>
    private static bool IsEntityRelevant(object entity)
    {
        // Basic relevance check - can be expanded based on entity type
        if (entity is Char)
        {
            Char character = (Char)entity;
            return PerformanceOptimizer.IsOnScreen(character.x, character.y, 50, 50);
        }
        else if (entity is Mob)
        {
            Mob mob = (Mob)entity;
            return PerformanceOptimizer.IsOnScreen(mob.x, mob.y, 50, 50);
        }
        
        return true; // Default to relevant if unsure
    }
    
    /// <summary>
    /// Get cached collection size to avoid repeated .size() calls
    /// </summary>
    private static int GetCachedSize(string key, int actualSize)
    {
        // Update cache every few frames
        if (Time.frameCount - sizeCacheFrame > 5)
        {
            cachedSizes[key] = actualSize;
            sizeCacheFrame = Time.frameCount;
        }
        
        return cachedSizes.ContainsKey(key) ? cachedSizes[key] : actualSize;
    }
    
    /// <summary>
    /// Optimized background updates
    /// </summary>
    public static void UpdateBackgroundOptimized()
    {
        backgroundUpdateFrame++;
        
        // Update background less frequently
        if (backgroundUpdateFrame % 3 == 0)
        {
            // Update scrolling backgrounds
            TileMap.updateBackground();
        }
        
        if (backgroundUpdateFrame % 5 == 0)
        {
            // Update background effects
            BackgroudEffect.updateEff();
        }
    }
    
    /// <summary>
    /// Optimized item map updates
    /// </summary>
    public static void UpdateItemMapOptimized(MyVector vItemMap)
    {
        if (vItemMap == null || PerformanceOptimizer.ShouldSkipUpdate("item_update", 3))
            return;
        
        int totalItems = GetCachedSize("items", vItemMap.size());
        
        // Only update items that are visible or important
        for (int i = 0; i < totalItems; i++)
        {
            ItemMap item = (ItemMap)vItemMap.elementAt(i);
            if (item != null && IsItemRelevant(item))
            {
                item.update();
            }
        }
    }
    
    /// <summary>
    /// Check if an item is relevant for updates
    /// </summary>
    private static bool IsItemRelevant(ItemMap item)
    {
        // Only update items that are on screen or recently dropped
        return PerformanceOptimizer.IsOnScreen(item.x, item.y, 20, 20) ||
               (item.timeAppear > 0 && GameCanvas.timeNow - item.timeAppear < 5000);
    }
    
    /// <summary>
    /// Set maximum entities to update per frame
    /// </summary>
    public static void SetMaxEntitiesPerFrame(int maxEntities)
    {
        maxEntitiesPerFrame = Math.Max(1, maxEntities);
    }
    
    /// <summary>
    /// Get performance statistics
    /// </summary>
    public static string GetOptimizationStats()
    {
        return $"Entity Batch: {currentEntityBatch}, " +
               $"Effect Frame: {effectUpdateFrame}, " +
               $"BG Frame: {backgroundUpdateFrame}, " +
               $"Max Entities/Frame: {maxEntitiesPerFrame}";
    }
}
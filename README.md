# Knowledge-is-Power (KIP)

A lightweight Fabric mod that lets players **learn super-powers from enchanted books** and cast them with a single key.  
No GUI, no clutter – just hold the book, right-click to learn, press **R** to unleash.

---

## 🌟 Features

| Power                  | Book                                | Effect                                           | Cool-down |
|------------------------|-------------------------------------|--------------------------------------------------|-----------|
| **Flame Burst**        | Enchanted Book – Flame              | Ignite what you’re looking at                    | 10 s      |
| **Instamine**          | Enchanted Book – Efficiency         | Haste, speed and instant-mining                  | 10 s      |
| **Flight**             | Enchanted Book – Feather Falling    | Flight                                           | 15 s      |
| **Lightning Strike**   | Enchanted Book – Channeling         | Call a lightning bolt where you look             | 10 s      |
| **Healing**            | Enchanted Book – Mending            | Heals hearts using experience                    | Passive   |
| **Invisibility**       | Enchanted Book – Curse of Vanishing | Invisibility                                     | 10s       |
| **Immutability**       | Enchanted Book - Unbreaking         | You become immutable briefly                     | 5s        |
| **Regeneration Field** | Enchanted Book - Protection         | Create an AOE of regeneration                    | 10s       |
| **Blast Protection**   | Enchanted Book - Blast Protection   | For a certain amount of times, blasts don't hurt | X times   |

*More powers can be added by any mod or data-pack – the system is 100 % data-driven.*

---

## 🎮 How to Play

1. **Craft / loot / enchant** any vanilla “Enchanted Book” with one of the enchantments above.
2. **Hold the book in your main-hand** and **right-click** → ability is learned, book is consumed (unless in creative).
3. Press **R** (default) to cast.
4. A small cooldown indicator appears in the hot-bar; another message tells you when the power is ready again.

---

## 🛠 Server Admin Notes

- **Pure server-side** – clients without the mod will be kicked, so distribute the jar to everyone.
- **No config file yet** – balancing is done via code (cooldown, duration).
- **Permissions** – any player can use any power; OP can revoke with `/clear` or `/effect clear`.
- **Performance** – all ray-casts are limited to 10 blocks and run only on use.

---

## 🧪 Debug Commands (OP only)

| Command | Purpose |
|---------|---------|
| `/kipmod debugbook` | Dumps full NBT of the book you’re holding to server console – useful when creating custom books. |

---
## 🧪 For Pack-Makers & Addon Devs

### ✅ Add a new enchantment → existing power (no code)
1. Create a **data-pack** (or include in your mod’s `data/`)
2. Add the enchantment to the desired tag:  
   `data/kipmod/tags/enchantment/grants_flame_ability.json`
   ```json
   {
     "replace": false,
     "values": [
       "your_mod:blazing_touch"
     ]
   }
   ```
   Done! Any book with `your_mod:blazing_touch` now grants **Flame Burst**.

---

### ✅ Add a brand-new ability (tiny Java snippet)

1. Implement `Abilities`:
   ```java
   public class VoidBlinkAbility implements Abilities {
       public String getId() { return "void_blink"; }
       public void activate(ServerPlayerEntity player){
           // your logic
       }
       // …other methods
   }
   ```
   Register it:
```java
AbilityRegistry.register("void_blink", new VoidBlinkAbility());
```
### Adding a custom ability

1. Create the settings file  
   `data/<yourmod>/ability_settings/<ability_name>.json`

   ```json
   {
     "id": "custom_ability",
     "durationTicks": 200,
     "cooldownTicks": 300,
     "range": 10.0,
     "fireSeconds": 4,
     "heartsPerOrb": 0.1,
     "orbDivisor": 10,
     "radius": 10,
     "times": 3
   }
   ```
Map it to an enchantment
Use the tag system (#yourmod:ability/custom_ability) or
Register a fallback mapping in your mod code.
# KIP Abilities – Developer README

## Advanced Features (Devs)

### Ability Settings
Every JSON file placed in `data/&lt;modid&gt;/ability_settings/` is loaded automatically.  
Supported keys and their meanings:

| JSON key        | Meaning |
|----------------|---------|
| `durationTicks` | Effect duration in ticks (20 tps) |
| `cooldownTicks` | Cool-down length in ticks |
| `range`         | Ray-cast range for targeted abilities |
| `fireSeconds`   | Fire duration in seconds |
| `heartsPerOrb`  | Health restored per XP orb (Mending) |
| `orbDivisor`    | XP value divisor (Mending) |
| `radius`        | AOE radius (Protection) |
| `times`         | Number of procs (Blast Protection) |

No additional registration code is required.

### Cardinal Components API Quick-start
```java
// get the player's current ability
Abilities ability = KIPModComponents.ABILITIES.get(player).getAbility();

// check cool-down
int cooldown = KIPModComponents.ABILITIES.get(player).getCooldown();

// attempt to fire the ability
boolean success = KIPModComponents.ABILITIES.get(player).tryUseAbility();
```
### Register Custom Events
Drop this in your mod initializer:

```java
// For damage blocking, XP collection, etc.
UnbreakingAbility.registerEvents();
BlastProtectionAbility.registerEvents();
```
---

## 📦 Installation
1. Install **Fabric-Loader ≥ 0.15** on client and server.  
2. Drop `knowledge-is-power-mod-x.x.x.jar` into the `mods` folder.  
3. *(Optional but recommended)* install **Fabric-API**.  
4. Start the game – no further configuration required.

---

## 🔗 Links

| What            | URL                                      |
|-----------------|------------------------------------------|
| Source & Issues | https://github.com/yourname/KnowledgeIsPower |
| CurseForge      | *(add once uploaded)*                    |
| Discord         | *(add once created)*                     |

---

## 📄 License
**MIT** – do whatever you want, just give credit.  
Made with Fabric by **Carmindy Creates**

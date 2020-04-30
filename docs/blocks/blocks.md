## Custom Blocks
#### What are Custom Blocks?
StarMade comes with 255 pre-defined spaces for custom blocks by 
default. Rather than add new blocks, the API will change the values
of these blocks to your custom ones.

#### API Classes
The `Block` class in element.block actually represents a physical
block on an entity, rather than a block type. The `Blocks` class
has all vanilla blocks as well as the aforementioned custom block
spaces. However, these custom block spaces are only actually
defined during loading, so if you want to reference a custom block
you'll have to go by it's name rather than ID.

## Creating your own Custom Block
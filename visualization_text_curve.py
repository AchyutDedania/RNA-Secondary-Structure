import matplotlib.pyplot as plt
import matplotlib.patches as patches

def draw_curve(filename):
    # Open the file
    with open(filename, 'r') as f:
        # Read the sequence from the first line
        sequence = f.readline().strip()
        
        # Read the pairs from the following lines
        pairs = []
        for line in f:
            pair = tuple(map(int, line.strip().split(',')))
            pairs.append(pair)
    
    # Initialize plot
    fig, ax = plt.subplots(figsize=(15, 10))
    
    # Plot points
    for i, nucleotide in enumerate(sequence):
        ax.scatter(i, 0, color='blue')  # Plot points along the x-axis
        ax.text(i, 0.5, nucleotide, ha='center', va='center')  # Label points with their nucleotide
    
    # Plot curves for each pair
    max_diameter = 0
    for pair in pairs:
        idx1, idx2 = pair
        x1, x2 = idx1, idx2
        y1, y2 = 0, 0
        diameter = abs(x2 - x1)
        max_diameter = max(max_diameter, diameter)
        arc = patches.Arc(((x1+x2)/2, 0), diameter, diameter, theta1=0, theta2=180, color='red')
        ax.add_patch(arc)
        plt.pause(1)
    
    # Set labels and title
    ax.set_xlabel('Index')
    ax.set_title('Curves between pairs of points')
    
    # Remove y-axis
    ax.axes.get_yaxis().set_visible(False)
    
    # Adjust y-limits
    ax.set_ylim(-max_diameter/2, max_diameter)
    
    # Show plot
    ax.grid(True)
    plt.savefig('plot_curve.png', dpi=300)  # Save plot as PNG file with 300 DPI
    plt.show()

# Example usage
draw_curve('output.txt')
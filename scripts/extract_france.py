# This script have been AI Generated, and has been used to create a JSON containing only personalities in france

import json
from shapely.geometry import shape, Point
from shapely.ops import unary_union

# 1. SETUP GEOGRAPHY
def load_france_boundary(geojson_path):
    print("Loading France boundary...")
    with open(geojson_path, 'r') as f:
        data = json.load(f)
        # Merge all regional polygons into one single 'France' shape
        shapes = [shape(feature['geometry']) for feature in data['features']]
        return unary_union(shapes)

france_poly = load_france_boundary('france.geojson')

# Quick Bounding Box check to speed up the script (skips global coordinates instantly)
# Approx: Longitude -5 to 10, Latitude 41 to 51
def is_roughly_in_france(lat, lon):
    return -5.5 <= lon <= 9.5 and 41.0 <= lat <= 51.5

def process_jsonl(input_file, output_file):
    count_total = 0
    count_saved = 0
    
    print(f"Starting processing: {input_file}...")
    
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        for line in infile:
            count_total += 1
            data = json.loads(line)
            
            # Filter places to keep only those inside France
            places_in_france = []
            for p in data.get("places", []):
                lat, lon = p.get("location")
                
                # Step 1: Fast bounding box check
                if is_roughly_in_france(lat, lon):
                    # Step 2: Precise polygon check
                    if france_poly.contains(Point(lon, lat)):
                        places_in_france.append(p)
            
            # If the person has at least one place in France, save the record
            if places_in_france:
                # Update the record with ONLY the French places
                filtered_record = {
                    "id": data.get("id"),
                    "name": data.get("name"),
                    "places": places_in_france
                }
                outfile.write(json.dumps(filtered_record) + '\n')
                count_saved += 1
            
            if count_total % 100000 == 0:
                print(f"Progress: {count_total} lines scanned... {count_saved} kept.")

    print(f"Done! Scanned {count_total} records. Saved {count_saved} French records to {output_file}.")

# Run it
process_jsonl('people-places.jsonl', 'france_only_data.jsonl')

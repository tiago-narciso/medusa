import json
import uuid
import io
import psycopg2
import sys

# Localhost or distant server address
host=sys.argv[1]
db=sys.argv[2]
user=sys.argv[3]
password=sys.argv[4]
jsonl=sys.argv[5]

def bulk_load(file_path):
    conn = psycopg2.connect(f"host={host} dbname={db} user={user} password={password}")
    cur = conn.cursor()

    # Buffers to hold CSV-formatted data in memory
    personality_buffer = io.StringIO()
    place_buffer = io.StringIO()

    print("Processing JSONL...")
    with open(file_path, 'r') as f:
        for line in f:
            data = json.loads(line)

            # 1. Generate a UUID for the personality now
            p_uuid = str(uuid.uuid4())
            wiki_id = str(data.get("id"))
            name = data.get("name", {}).get("en", "Unknown").replace('"', '""')

            # Find POB name
            pob_list = [p for p in data.get("places", []) if p.get("relation_id") == 19]
            pob_name = pob_list[0]["name"].get("en", "").replace('"', '""') if pob_list else ""

            # 2. Add to Personality Buffer (CSV Format: id, wikidata_id, name, place_of_birth)
            personality_buffer.write(f'"{p_uuid}","{wiki_id}","{name}","{pob_name}"\n')

            # 3. Add to Place Buffer (CSV Format: is_zone, personality_id, position)
            for p in data.get("places", []):
                is_zone = str(p.get("zone", False)).upper()
                lat, lon = p.get("location")
                # Format for PostGIS: SRID=4326;POINT(lon lat)
                point_wkt = f"SRID=4326;POINT({lon} {lat})"
                place_buffer.write(f'{is_zone},"{p_uuid}","{point_wkt}"\n')

    # Seek back to start of buffers
    personality_buffer.seek(0)
    place_buffer.seek(0)

    print("Bulk inserting personalities...")
    cur.copy_expert("""
        COPY personality (id, wikidata_id, name, place_of_birth) 
        FROM STDIN WITH (FORMAT CSV, QUOTE '"')
    """, personality_buffer)

    print("Bulk inserting places...")
    cur.copy_expert("""
        COPY place (is_zone, personality_id, position) 
        FROM STDIN WITH (FORMAT CSV, QUOTE '"')
    """, place_buffer)

    conn.commit()
    cur.close()
    conn.close()
    print("Done!")

bulk_load(jsonl)

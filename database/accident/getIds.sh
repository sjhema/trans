ids=''
for file in *; do
    if [ -f "$file" ]; then
        ids="$ids, $(echo "$file" | cut -f1 -d'_')"

    fi
done
echo $ids